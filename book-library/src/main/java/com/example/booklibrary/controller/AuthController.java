package com.example.booklibrary.controller;

import com.example.booklibrary.dto.RegisterRequest;
import com.example.booklibrary.model.User;
import com.example.booklibrary.repository.UserRepository;
import com.example.booklibrary.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Registration and current-user info (login itself is handled by Spring Security's form login)")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        User savedUser = userService.register(
                request.getName(), request.getEmail(), request.getAge(), request.getPassword()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedUser.getId());
        response.put("name", savedUser.getName());
        response.put("email", savedUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Returns basic info about the currently logged-in user.
     * Used by the frontend to greet the user by name and know their age
     * (age isn't used to restrict anything yet — it's just tracked for now).
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> currentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("age", user.getAge());
        return ResponseEntity.ok(response);
    }

    /**
     * Logout endpoint. Spring Security's logout filter will invalidate the session
     * and clear cookies before this method is called. This endpoint just confirms
     * the logout was successful.
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
}
