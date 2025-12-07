package com.bms.auth.service;

import com.bms.auth.dto.LoginDaoDTO;
import com.bms.auth.dto.LoginRequest;
import com.bms.auth.dto.LoginResponse;
import com.bms.auth.model.User;
import com.bms.auth.repository.LoginDao;
import com.bms.common.exception.UnauthorizedException;
import com.bms.common.util.BodyJWT;
import com.bms.common.util.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao dao;
    
    @Autowired
    private ObjectMapper objectMapper;  // Singleton - injected by Spring

    @Override
    public LoginResponse login(LoginRequest req) {
        String email = req.getEmail();
        String password = req.getPassword();
        String hashedPassword = DigestUtils.sha256Hex(password);
        
        User user = dao.getUserInfo(new LoginDaoDTO(email, hashedPassword));
        
        // Check if user exists and password matches
        if (user == null || !Objects.equals(user.getPassword(), hashedPassword)) {
            throw new UnauthorizedException("Invalid email or password");
        }
        
        // Successful login
        Date expiryDate = new Date(System.currentTimeMillis() + 3600000); // 1 hour
        BodyJWT body = BodyJWT.builder()
                .sub(user.getEmail())
                .email(user.getEmail())
                .userId(user.getId())
                .exp(expiryDate)
                .build();
        
        String token = JWT.getToken(body);
        
        // Create response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("expiryEpoch", expiryDate.getTime()); // Convert Date to epoch (long)
        
        try {
            String jsonData = objectMapper.writeValueAsString(responseData);
            return LoginResponse.builder()
                    .statusCode(200)  // 200 OK for successful login
                    .data(jsonData)
                    .build();
        } catch (JsonProcessingException e) {
            // This is a rare case - log and rethrow as runtime exception
            System.err.println("Error serializing response: " + e.getMessage());
            throw new RuntimeException("Failed to serialize response", e);
        }
    }
}

