package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    // Method to get a new connection (called for each database operation)
    public Connection getConnection() throws SQLException {
        // Load PostgreSQL driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
        
        // Create and return a new connection
        return DriverManager.getConnection(url, username, password);
    }
}

