package com.example.booklibrary.service;

import com.example.booklibrary.exception.EmailAlreadyExistsException;
import com.example.booklibrary.model.User;
import com.example.booklibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user. The raw password is hashed with BCrypt before
     * being stored — the plain-text password is never persisted anywhere.
     */
    public User register(String name, String email, int age, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        String hashed = passwordEncoder.encode(rawPassword);
        User user = new User(name, email, age, hashed);
        return userRepository.save(user);
    }
}
