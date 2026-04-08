package com.klu.service;


import com.klu.entity.User;
import com.klu.exception.ConflictException;
import com.klu.exception.UnauthorizedException;
import com.klu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        if (user == null) throw new IllegalArgumentException("User is required");
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("Email already registered");
        }

        if (user.getRole() != null) {
            user.setRole(user.getRole().toLowerCase());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
        
        return userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }
}
