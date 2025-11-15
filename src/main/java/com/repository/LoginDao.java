package com.repository;

import com.config.DatabaseConfig;
import com.dto.LoginDaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LoginDao {
    
    @Autowired
    private DatabaseConfig databaseConfig;
    
    public boolean validate(LoginDaoDTO req) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            // Step 1: Get database connection
            connection = databaseConfig.getConnection();
            
            // Debug: Check which database we're connected to
            String currentDb = connection.getCatalog();
            System.out.println("LoginDao connected to database: " + currentDb);
            
            // Step 2: Create prepared statement with parameterized query (prevents SQL injection)
            String sql = "SELECT id, email, password, username, phone FROM users WHERE email = ?";
            statement = connection.prepareStatement(sql);
            
            // Step 3: Set parameters
            statement.setString(1, req.getEmail());
            
            // Step 4: Execute query
            resultSet = statement.executeQuery();
            
            // Step 5: Process results
            if (resultSet.next()) {
                // User found - get password from database
                String dbPassword = resultSet.getString("password");
                
                // Compare passwords
                return dbPassword.equals(req.getPassword());
            } else {
                // User not found
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Step 6: Close all resources (important to prevent leaks)
            closeResources(connection, statement, resultSet);
        }
    }
    
    // Helper method to close resources
    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

