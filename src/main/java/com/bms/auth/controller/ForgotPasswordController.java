package com.bms.auth.controller;

import com.bms.auth.dto.ForgotPasswordRequest;
import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.ResetPasswordRequest;
import com.bms.auth.dto.VerifyCodeRequest;
import com.bms.auth.service.ForgotPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForgotPasswordController {
    
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    
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

