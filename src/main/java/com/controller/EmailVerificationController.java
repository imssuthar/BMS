package com.controller;

import com.dto.LoginResponse;
import com.dto.VerifyEmailRequest;
import com.service.EmailVerificationI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailVerificationController {
    
    @Autowired
    private EmailVerificationI emailVerificationService;
    
    @PostMapping("/verify-email")
    public ResponseEntity<Object> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        LoginResponse resp = emailVerificationService.verifyEmail(request);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getData());
    }
}

