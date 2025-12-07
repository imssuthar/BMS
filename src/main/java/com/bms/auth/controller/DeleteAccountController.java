package com.bms.auth.controller;

import com.bms.auth.dto.LoginResponse;
import com.bms.auth.service.DeleteAccountService;
import com.bms.common.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteAccountController {
    
    @Autowired
    private DeleteAccountService deleteAccountService;
    
    @DeleteMapping("/delete-account")
    public ResponseEntity<Object> deleteAccount(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // Extract token from "Bearer <token>" format
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization token required");
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        
        // No try-catch needed - exceptions are handled by GlobalExceptionHandler
        LoginResponse resp = deleteAccountService.deleteAccount(token);
        return ResponseEntity.ok(resp.getData());
    }
}

