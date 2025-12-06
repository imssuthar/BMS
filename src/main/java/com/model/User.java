package com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                    // Marks this as a database table
@Table(name = "users")     // Table name in database
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id                                                    // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @Column(unique = true, nullable = false)  // Unique and required
    private String email;
    
    @Column(nullable = false)                  // Required
    private String password;
    
    @Column(nullable = false)
    private Boolean verified = false;  // Email verification status
    
    private String username;
    private String phone;
}

