package com.util;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BodyJWT {
    private String sub;        // Subject (user email/id)
    private Date iat;          // Issued at
    private Date exp;           // Expiration time
    private String email;       // User email
    private Long userId;        // User ID
}

