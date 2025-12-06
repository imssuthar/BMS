package com.service;

import com.dto.LoginResponse;
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
        
        // Validate token
        if (bodyJWT == null) {
            return createErrorResponse(401, "Invalid or expired token");
        }
        
        // Get user from database
        User user = userRepository.findByEmail(bodyJWT.getEmail())
                .orElse(null);
        
        if (user == null) {
            return createErrorResponse(404, "User not found");
        }
        
        // Verify token userId matches user id (extra security check)
        if (!user.getId().equals(bodyJWT.getUserId())) {
            return createErrorResponse(403, "Token does not match user account");
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
            System.err.println("Error serializing response: " + e.getMessage());
            return createErrorResponse(500, "Internal server error");
        }
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
            System.err.println("Error serializing error response: " + e.getMessage());
            return LoginResponse.builder()
                    .statusCode(500)
                    .data("{\"respText\":\"Internal server error\"}")
                    .build();
        }
    }
}

