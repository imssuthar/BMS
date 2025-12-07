package com.bms.auth.service;

import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.SignUpRequest;
import com.bms.auth.model.User;
import com.bms.auth.repository.UserRepository;
import com.bms.common.exception.ConflictException;
import com.bms.common.util.EmailService;
import com.bms.common.util.VerificationCodeStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SignUpServiceImpl implements SignUpService {

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
            throw new ConflictException("Email", req.getEmail());
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

