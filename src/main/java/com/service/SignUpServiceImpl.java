package com.service;

import com.dto.LoginResponse;
import com.dto.SignUpRequest;
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
public class SignUpServiceImpl implements SignUpI {

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
    public LoginResponse signUp(SignUpRequest req) {
        // Check if email already exists
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return createErrorResponse(400, "Email already exists");
        }
        
        // Hash password
        String hashedPassword = DigestUtils.sha256Hex(req.getPassword());
        
        // Create new user (not verified initially)
        User newUser = new User();
        newUser.setEmail(req.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setVerified(false);  // Email not verified yet
        
        // Save user to database
        userRepository.save(newUser);
        
        // Generate 6-digit verification code
        String code = generateVerificationCode();
        
        // Store code with expiry time
        long expiryTime = System.currentTimeMillis() + (CODE_EXPIRY_MINUTES * 60 * 1000);
        codeStorage.storeEmailVerificationCode(req.getEmail(), code, expiryTime);
        
        // Send email verification code
        emailService.sendEmailVerificationCode(req.getEmail(), code);
        
        // Create success response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("respText", "Account created successfully. Please check your email for verification code.");
        
        try {
            String jsonData = objectMapper.writeValueAsString(responseData);
            return LoginResponse.builder()
                    .statusCode(201)  // 201 Created for new user registration
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
}

