package com.example.dmart.service;

import com.example.dmart.exception.EmailAlreadyExistsException;
import com.example.dmart.model.User;
import com.example.dmart.repository.UserRepository;
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

    public User register(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        String hashed = passwordEncoder.encode(rawPassword);
        User user = new User(name, email, hashed);
        return userRepository.save(user);
    }
}
