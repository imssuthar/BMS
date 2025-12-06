package com.controller;

import com.dto.LoginResponse;
import com.service.DeleteAccountI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteAccountController {
    
    @Autowired
    private DeleteAccountI deleteAccountService;
    
    @DeleteMapping("/delete-account")
    public ResponseEntity<Object> deleteAccount(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // Extract token from "Bearer <token>" format
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("{\"respText\":\"Authorization token required\"}");
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        
        LoginResponse resp = deleteAccountService.deleteAccount(token);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getData());
    }
}

