package com.controller;

import com.dto.ForgotPasswordRequest;
import com.dto.LoginResponse;
import com.dto.ResetPasswordRequest;
import com.dto.VerifyCodeRequest;
import com.service.ForgotPasswordI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForgotPasswordController {
    
    @Autowired
    private ForgotPasswordI forgotPasswordService;
    
    @PostMapping("/forgot-password")
    public ResponseEntity<Object> requestPasswordReset(@RequestBody @Valid ForgotPasswordRequest request) {
        // No try-catch needed - exceptions handled by GlobalExceptionHandler
        LoginResponse resp = forgotPasswordService.requestPasswordReset(request);
        return ResponseEntity.ok(resp.getData());
    }
    
    @PostMapping("/verify-reset-code")
    public ResponseEntity<Object> verifyCode(@RequestBody @Valid VerifyCodeRequest request) {
        // No try-catch needed - exceptions handled by GlobalExceptionHandler
        LoginResponse resp = forgotPasswordService.verifyCode(request);
        return ResponseEntity.ok(resp.getData());
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        // No try-catch needed - exceptions handled by GlobalExceptionHandler
        LoginResponse resp = forgotPasswordService.resetPassword(request);
        return ResponseEntity.ok(resp.getData());
    }
}

