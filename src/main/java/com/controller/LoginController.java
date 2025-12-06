package com.controller;

import com.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dto.LoginRequest;
import com.service.LoginI;

@RestController
public class LoginController {
    @Autowired
    private LoginI loginService;
    
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest loginRequest) {
        // No try-catch needed - exceptions handled by GlobalExceptionHandler
        LoginResponse resp = loginService.login(loginRequest);
        return ResponseEntity.ok(resp.getData());
    }
}
