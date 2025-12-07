package com.bms.auth.repository;

import com.bms.auth.dto.LoginDaoDTO;
import com.bms.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginDao {
    
    @Autowired
    private UserRepository userRepository;  // Inject repository instead of DatabaseConfig
    
    public User getUserInfo(LoginDaoDTO req) {
        // Find user by email (JPA handles all SQL automatically!)
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
        
        if (userOpt.isEmpty()) {
            return null;  // User not found
        }
        
        return userOpt.get();
    }
}

