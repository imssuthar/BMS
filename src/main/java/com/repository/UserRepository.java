package com.repository;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring automatically generates implementation!
    // Method name "findByEmail" → generates SQL: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
    Optional<Boolean> findByEmailAndPassword(String email , String password);
}

