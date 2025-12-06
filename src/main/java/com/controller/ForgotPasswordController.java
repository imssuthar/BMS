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
        LoginResponse resp = forgotPasswordService.requestPasswordReset(request);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getData());
    }
    
    @PostMapping("/verify-reset-code")
    public ResponseEntity<Object> verifyCode(@RequestBody @Valid VerifyCodeRequest request) {
        LoginResponse resp = forgotPasswordService.verifyCode(request);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getData());
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        LoginResponse resp = forgotPasswordService.resetPassword(request);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getData());
    }
}

