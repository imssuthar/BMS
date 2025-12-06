package com.service;

import com.dto.ForgotPasswordRequest;
import com.dto.LoginResponse;
import com.dto.ResetPasswordRequest;
import com.dto.VerifyCodeRequest;
import com.model.User;
import com.repository.UserRepository;
import com.util.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordI {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private EmailService emailService;
    
    // In-memory storage for verification codes: email -> {code, expiryTime}
    // In production, use Redis or database for distributed systems
    private final Map<String, CodeInfo> verificationCodes = new ConcurrentHashMap<>();
    
    private static final int CODE_EXPIRY_MINUTES = 10; // Code expires in 10 minutes
    
    @Override
    public LoginResponse requestPasswordReset(ForgotPasswordRequest request) {
        String email = request.getEmail();
        
        // Check if user exists
        if (userRepository.findByEmail(email).isEmpty()) {
            return createErrorResponse(404, "Email not found");
        }
        
        // Generate 6-digit verification code
        String code = generateVerificationCode();
        
        // Store code with expiry time
        long expiryTime = System.currentTimeMillis() + (CODE_EXPIRY_MINUTES * 60 * 1000);
        verificationCodes.put(email, new CodeInfo(code, expiryTime));
        
        // Send email with code
        emailService.sendVerificationCode(email, code);
        
        // Return success response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("respText", "Verification code sent to your email");
        
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
    
    @Override
    public LoginResponse verifyCode(VerifyCodeRequest request) {
        String email = request.getEmail();
        String providedCode = request.getCode();
        
        CodeInfo codeInfo = verificationCodes.get(email);
        
        // Check if code exists
        if (codeInfo == null) {
            return createErrorResponse(400, "No verification code found. Please request a new code.");
        }
        
        // Check if code expired
        if (System.currentTimeMillis() > codeInfo.getExpiryTime()) {
            verificationCodes.remove(email); // Clean up expired code
            return createErrorResponse(400, "Verification code has expired. Please request a new code.");
        }
        
        // Verify code
        if (!codeInfo.getCode().equals(providedCode)) {
            return createErrorResponse(400, "Invalid verification code");
        }
        
        // Code is valid
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("respText", "Code verified successfully");
        
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
    
    @Override
    public LoginResponse resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String providedCode = request.getCode();
        String newPassword = request.getNewPassword();
        
        CodeInfo codeInfo = verificationCodes.get(email);
        
        // Check if code exists
        if (codeInfo == null) {
            return createErrorResponse(400, "No verification code found. Please request a new code.");
        }
        
        // Check if code expired
        if (System.currentTimeMillis() > codeInfo.getExpiryTime()) {
            verificationCodes.remove(email); // Clean up expired code
            return createErrorResponse(400, "Verification code has expired. Please request a new code.");
        }
        
        // Verify code
        if (!codeInfo.getCode().equals(providedCode)) {
            return createErrorResponse(400, "Invalid verification code");
        }
        
        // Code is valid, reset password
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")); // Should not happen
        
        // Hash new password
        String hashedPassword = DigestUtils.sha256Hex(newPassword);
        user.setPassword(hashedPassword);
        
        // Save updated user
        userRepository.save(user);
        
        // Remove used code
        verificationCodes.remove(email);
        
        // Return success response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("respText", "Password reset successfully");
        
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
    
    // Helper method to generate 6-digit verification code
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates 100000-999999
        return String.valueOf(code);
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
    
    // Inner class to store code and expiry time
    private static class CodeInfo {
        private final String code;
        private final long expiryTime;
        
        public CodeInfo(String code, long expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }
        
        public String getCode() {
            return code;
        }
        
        public long getExpiryTime() {
            return expiryTime;
        }
    }
}

