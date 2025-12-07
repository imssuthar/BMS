package com.bms.auth.controller;

import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.SignUpRequest;
import com.bms.auth.service.SignUpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {
    
    @Autowired
    private SignUpService signUpService;
    
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        // No try-catch needed - exceptions handled by GlobalExceptionHandler
        LoginResponse resp = signUpService.signUp(signUpRequest);
        return ResponseEntity.status(201).body(resp.getData()); // 201 Created for signup
    }
}

