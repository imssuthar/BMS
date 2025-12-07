package com.bms.auth.controller;

import com.bms.auth.dto.LoginRequest;
import com.bms.auth.dto.LoginResponse;
import com.bms.auth.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest loginRequest) {
        // No try-catch needed - exceptions handled by GlobalExceptionHandler
        LoginResponse resp = loginService.login(loginRequest);
        return ResponseEntity.ok(resp.getData());
    }
}

