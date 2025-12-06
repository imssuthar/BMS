package com.service;


import com.dto.LoginDaoDTO;
import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repository.LoginDao;
import com.util.JWT;
import com.util.BodyJWT;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginI {

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
        
        if(user != null && Objects.equals(user.getPassword(), hashedPassword)) {
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
                // Log error and return error response
                System.err.println("Error serializing response: " + e.getMessage());
                return createErrorResponse(500, "Internal server error");
            }
        }
        
        // Invalid credentials
        return createErrorResponse(400, "Invalid email or password");
    }
    
    // Helper method to avoid code duplication
    private LoginResponse createErrorResponse(int statusCode, String message) {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("respText", message);
        
        try {
            String jsonData = objectMapper.writeValueAsString(errorData);
            return LoginResponse.builder()
                    .statusCode(statusCode)
                    .data(jsonData)
                    .build();
        } catch (JsonProcessingException e) {
            // Fallback if JSON serialization fails
            System.err.println("Error serializing error response: " + e.getMessage());
            return LoginResponse.builder()
                    .statusCode(500)
                    .data("{\"respText\":\"Internal server error\"}")
                    .build();
        }
    }
}
