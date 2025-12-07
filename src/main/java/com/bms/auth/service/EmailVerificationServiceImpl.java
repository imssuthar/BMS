package com.bms.auth.service;

import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.VerifyEmailRequest;
import com.bms.auth.model.User;
import com.bms.auth.repository.UserRepository;
import com.bms.common.exception.ConflictException;
import com.bms.common.exception.ResourceNotFoundException;
import com.bms.common.exception.ValidationException;
import com.bms.common.util.EmailService;
import com.bms.common.util.VerificationCodeStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

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
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        
        // Check if already verified
        if (Boolean.TRUE.equals(user.getVerified())) {
            throw new ConflictException("Email already verified");
        }
        
        // Get stored code info
        VerificationCodeStorage.CodeInfo codeInfo = codeStorage.getEmailVerificationCode(email);
        
        // Check if code exists
        if (codeInfo == null) {
            throw new ValidationException("No verification code found. Please request a new code.");
        }
        
        // Check if code expired
        if (System.currentTimeMillis() > codeInfo.getExpiryTime()) {
            codeStorage.removeEmailVerificationCode(email); // Clean up expired code
            throw new ValidationException("Verification code has expired. Please request a new code.");
        }
        
        // Verify code
        if (!codeInfo.getCode().equals(providedCode)) {
            throw new ValidationException("Invalid verification code");
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
            throw new RuntimeException("Failed to serialize response", e);
        }
    }
}

