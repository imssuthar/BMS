package com.bms.auth.controller;

import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.VerifyEmailRequest;
import com.bms.auth.service.EmailVerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailVerificationController {
    
    @Autowired
    private EmailVerificationService emailVerificationService;
    
    @PostMapping("/verify-email")
    public ResponseEntity<Object> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        // No try-catch needed - exceptions handled by GlobalExceptionHandler
        LoginResponse resp = emailVerificationService.verifyEmail(request);
        return ResponseEntity.ok(resp.getData());
    }
}

