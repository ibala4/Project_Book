package com.example.booklibrary.config;

import com.example.booklibrary.model.Book;
import com.example.booklibrary.model.User;
import com.example.booklibrary.repository.BookRepository;
import com.example.booklibrary.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(BookRepository bookRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Only seed sample data if the table is currently empty.
        // Prevents duplicate rows being inserted on every app restart.
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("Clean Code", "Robert C. Martin", "Programming", 2008, 3, 17));
            bookRepository.save(new Book("Effective Java", "Joshua Bloch", "Programming", 2017, 5, 0));
            bookRepository.save(new Book("Spring in Action", "Craig Walls", "Programming", 2020, 0, 0));
            bookRepository.save(new Book("Atomic Habits", "James Clear", "Self-help", 2018, 2, 0));
        }

        // Seed two test logins so you can try both sides of the age cap
        // without registering first. Passwords are hashed with BCrypt
        // before storing, same as a real registration would do.
        if (userRepository.count() == 0) {
            String hashed = passwordEncoder.encode("test1234");
            userRepository.save(new User("Young User", "young@example.com", 15, hashed));
            userRepository.save(new User("Adult User", "adult@example.com", 25, hashed));
            System.out.println("Seeded test logins (password for both: test1234):");
            System.out.println("  young@example.com (age 15) -> sees every book, including Clean Code (capped at 17)");
            System.out.println("  adult@example.com (age 25) -> Clean Code is hidden (25 > cap of 17)");
        }
    }
}
