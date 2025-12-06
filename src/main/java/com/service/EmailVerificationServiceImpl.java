package com.service;

import com.dto.LoginResponse;
import com.dto.VerifyEmailRequest;
import com.model.User;
import com.repository.UserRepository;
import com.util.EmailService;
import com.util.VerificationCodeStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationI {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private VerificationCodeStorage codeStorage;
    
    @Override
    public LoginResponse verifyEmail(VerifyEmailRequest request) {
        String email = request.getEmail();
        String providedCode = request.getCode();
        
        // Check if user exists
        User user = userRepository.findByEmail(email)
                .orElse(null);
        
        if (user == null) {
            return createErrorResponse(404, "User not found");
        }
        
        // Check if already verified
        if (Boolean.TRUE.equals(user.getVerified())) {
            return createErrorResponse(400, "Email already verified");
        }
        
        // Get stored code info
        VerificationCodeStorage.CodeInfo codeInfo = codeStorage.getEmailVerificationCode(email);
        
        // Check if code exists
        if (codeInfo == null) {
            return createErrorResponse(400, "No verification code found. Please request a new code.");
        }
        
        // Check if code expired
        if (System.currentTimeMillis() > codeInfo.getExpiryTime()) {
            codeStorage.removeEmailVerificationCode(email); // Clean up expired code
            return createErrorResponse(400, "Verification code has expired. Please request a new code.");
        }
        
        // Verify code
        if (!codeInfo.getCode().equals(providedCode)) {
            return createErrorResponse(400, "Invalid verification code");
        }
        
        // Code is valid, mark email as verified
        user.setVerified(true);
        userRepository.save(user);
        
        // Remove used code
        codeStorage.removeEmailVerificationCode(email);
        
        // Send welcome email after successful verification
        emailService.sendWelcomeEmail(email);
        
        // Return success response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("respText", "Email verified successfully. Your account is now active!");
        
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

