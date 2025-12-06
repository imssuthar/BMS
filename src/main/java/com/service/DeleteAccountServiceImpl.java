package com.service;

import com.dto.LoginResponse;
import com.exception.ForbiddenException;
import com.exception.ResourceNotFoundException;
import com.exception.UnauthorizedException;
import com.model.User;
import com.repository.UserRepository;
import com.util.BodyJWT;
import com.util.EmailService;
import com.util.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeleteAccountServiceImpl implements DeleteAccountI {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private EmailService emailService;

    @Override
    public LoginResponse deleteAccount(String token) {
        // Parse JWT token
        BodyJWT bodyJWT = JWT.parseToken(token);
        
        // Validate token - throw exception instead of returning error response
        if (bodyJWT == null) {
            throw new UnauthorizedException("Invalid or expired token");
        }
        
        // Get user from database - throw exception if not found
        User user = userRepository.findByEmail(bodyJWT.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", bodyJWT.getEmail()));
        
        // Verify token userId matches user id (extra security check)
        if (!user.getId().equals(bodyJWT.getUserId())) {
            throw new ForbiddenException("Token does not match user account");
        }
        
        // Send account deletion confirmation email
        emailService.sendAccountDeletionEmail(user.getEmail());
        
        // Delete user from database
        userRepository.delete(user);
        
        // Return success response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("respText", "Account deleted successfully");
        
        try {
            String jsonData = objectMapper.writeValueAsString(responseData);
            return LoginResponse.builder()
                    .statusCode(200)
                    .data(jsonData)
                    .build();
        } catch (JsonProcessingException e) {
            // This is a rare case - log and rethrow as runtime exception
            System.err.println("Error serializing response: " + e.getMessage());
            throw new RuntimeException("Failed to serialize response", e);
        }
    }
}

