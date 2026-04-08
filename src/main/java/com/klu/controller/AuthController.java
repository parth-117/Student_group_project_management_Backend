package com.klu.controller;



import com.klu.dto.AuthDtos;
import com.klu.entity.User;
import com.klu.service.AuthService;
import com.klu.config.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.AuthResponse> register(@RequestBody AuthDtos.RegisterRequest body) {
        User user = new User();
        user.setName(body.getName());
        user.setEmail(body.getEmail());
        user.setPassword(body.getPassword());
        user.setRole(body.getRole());
        if (body.getEnrollments() != null) {
            user.setEnrollments(body.getEnrollments());
        }
        User registeredUser = authService.register(user);
        
        String token = jwtUtil.generateToken(registeredUser.getEmail());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(@RequestBody AuthDtos.LoginRequest body) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword())
        );

        User user = authService.login(body.getEmail(), body.getPassword());
        String token = jwtUtil.generateToken(body.getEmail());

        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, user));
    }
}
