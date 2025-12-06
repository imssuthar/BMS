package com.controller;

import com.dto.LoginResponse;
import com.dto.SignUpRequest;
import com.service.SignUpI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SignUpController {
    
    @Autowired
    private SignUpI signUpService;
    
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        LoginResponse resp = signUpService.signUp(signUpRequest);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getData());
    }
}
