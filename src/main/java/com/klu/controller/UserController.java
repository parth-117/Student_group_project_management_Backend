package com.klu.controller;

import com.klu.entity.User;
import com.klu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;

    @Autowired
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<User> list(@RequestParam(value = "role", required = false) String role) {
        if (role == null || role.isBlank()) return repo.findAll();
        return repo.findByRoleIgnoreCase(role);
    }
}

