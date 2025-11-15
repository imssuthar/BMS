package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Simple POJO - no JPA annotations (raw JDBC approach)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String username;
    private String phone;
}

