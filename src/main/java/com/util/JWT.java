package com.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWT {
    private static final String SECRET_KEY = "12312312312312312312312312312312"; // Must be at least 32 chars for HS256
    
    public static String getToken(BodyJWT body) {
        // Create secret key from string
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        
        // Build claims (payload)
        Map<String, Object> claims = new HashMap<>();
        if (body != null) {
            if (body.getSub() != null) claims.put("sub", body.getSub());
            if (body.getEmail() != null) claims.put("email", body.getEmail());
            if (body.getUserId() != null) claims.put("userId", body.getUserId());
        }
        
        Date issuedAt = body != null && body.getIat() != null ? body.getIat() : new Date();
        Date expiration = body != null && body.getExp() != null ? body.getExp() : new Date(System.currentTimeMillis() + 3600000); // 1 hour
        
        // Build JWT
        // Note: Header is automatically created by jjwt library as:
        // { "alg": "HS256", "typ": "JWT" }
        // The header parameter is optional - library handles it automatically
        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)  // This automatically sets header.alg = "HS256"
                .compact();     // Creates: base64Url(header) + "." + base64Url(payload) + "." + signature
    }
    
    // Method to validate and parse token
    public static BodyJWT parseToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return BodyJWT.builder()
                    .sub(claims.get("sub", String.class))
                    .email(claims.get("email", String.class))
                    .userId(claims.get("userId", Long.class))
                    .iat(claims.getIssuedAt())
                    .exp(claims.getExpiration())
                    .build();
        } catch (Exception e) {
            return null; // Invalid token
        }
    }
}
