package com.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared storage for verification codes (email verification and password reset)
 * In production, use Redis or database for distributed systems
 */
@Component
public class VerificationCodeStorage {
    
    // Storage: email -> {code, expiryTime}
    private final Map<String, CodeInfo> emailVerificationCodes = new ConcurrentHashMap<>();
    private final Map<String, CodeInfo> passwordResetCodes = new ConcurrentHashMap<>();
    
    // Email verification codes
    public void storeEmailVerificationCode(String email, String code, long expiryTime) {
        emailVerificationCodes.put(email, new CodeInfo(code, expiryTime));
    }
    
    public CodeInfo getEmailVerificationCode(String email) {
        return emailVerificationCodes.get(email);
    }
    
    public void removeEmailVerificationCode(String email) {
        emailVerificationCodes.remove(email);
    }
    
    // Password reset codes
    public void storePasswordResetCode(String email, String code, long expiryTime) {
        passwordResetCodes.put(email, new CodeInfo(code, expiryTime));
    }
    
    public CodeInfo getPasswordResetCode(String email) {
        return passwordResetCodes.get(email);
    }
    
    public void removePasswordResetCode(String email) {
        passwordResetCodes.remove(email);
    }
    
    // Inner class to store code and expiry time
    public static class CodeInfo {
        private final String code;
        private final long expiryTime;
        
        public CodeInfo(String code, long expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }
        
        public String getCode() {
            return code;
        }
        
        public long getExpiryTime() {
            return expiryTime;
        }
    }
}

