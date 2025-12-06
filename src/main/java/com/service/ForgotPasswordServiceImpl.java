package com.service;

import com.dto.ForgotPasswordRequest;
import com.dto.LoginResponse;
import com.dto.ResetPasswordRequest;
import com.dto.VerifyCodeRequest;
import com.exception.ResourceNotFoundException;
import com.exception.ValidationException;
import com.model.User;
import com.repository.UserRepository;
import com.util.EmailService;
import com.util.VerificationCodeStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordI {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private VerificationCodeStorage codeStorage;
    
    private static final int CODE_EXPIRY_MINUTES = 10; // Code expires in 10 minutes
    
    @Override
    public LoginResponse requestPasswordReset(ForgotPasswordRequest request) {
        String email = request.getEmail();
        
        // Check if user exists
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new ResourceNotFoundException("User", email);
        }
        
        // Generate 6-digit verification code
        String code = generateVerificationCode();
        
        // Store code with expiry time
        long expiryTime = System.currentTimeMillis() + (CODE_EXPIRY_MINUTES * 60 * 1000);
        codeStorage.storePasswordResetCode(email, code, expiryTime);
        
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
            throw new RuntimeException("Failed to serialize response", e);
        }
    }
    
    @Override
    public LoginResponse verifyCode(VerifyCodeRequest request) {
        String email = request.getEmail();
        String providedCode = request.getCode();
        
        VerificationCodeStorage.CodeInfo codeInfo = codeStorage.getPasswordResetCode(email);
        
        // Check if code exists
        if (codeInfo == null) {
            throw new ValidationException("No verification code found. Please request a new code.");
        }
        
        // Check if code expired
        if (System.currentTimeMillis() > codeInfo.getExpiryTime()) {
            codeStorage.removePasswordResetCode(email); // Clean up expired code
            throw new ValidationException("Verification code has expired. Please request a new code.");
        }
        
        // Verify code
        if (!codeInfo.getCode().equals(providedCode)) {
            throw new ValidationException("Invalid verification code");
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
            throw new RuntimeException("Failed to serialize response", e);
        }
    }
    
    @Override
    public LoginResponse resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String providedCode = request.getCode();
        String newPassword = request.getNewPassword();
        
        VerificationCodeStorage.CodeInfo codeInfo = codeStorage.getPasswordResetCode(email);
        
        // Check if code exists
        if (codeInfo == null) {
            throw new ValidationException("No verification code found. Please request a new code.");
        }
        
        // Check if code expired
        if (System.currentTimeMillis() > codeInfo.getExpiryTime()) {
            codeStorage.removePasswordResetCode(email); // Clean up expired code
            throw new ValidationException("Verification code has expired. Please request a new code.");
        }
        
        // Verify code
        if (!codeInfo.getCode().equals(providedCode)) {
            throw new ValidationException("Invalid verification code");
        }
        
        // Code is valid, reset password
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        
        // Hash new password
        String hashedPassword = DigestUtils.sha256Hex(newPassword);
        user.setPassword(hashedPassword);
        
        // Save updated user
        userRepository.save(user);
        
        // Remove used code
        codeStorage.removePasswordResetCode(email);
        
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
            throw new RuntimeException("Failed to serialize response", e);
        }
    }
    
    // Helper method to generate 6-digit verification code
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates 100000-999999
        return String.valueOf(code);
    }
}

