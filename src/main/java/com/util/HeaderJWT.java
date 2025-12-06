package com.util;

import lombok.Data;

@Data
public class HeaderJWT {
    private String alg = "HS256";  // Algorithm
    private String typ = "JWT";    // Type
}

