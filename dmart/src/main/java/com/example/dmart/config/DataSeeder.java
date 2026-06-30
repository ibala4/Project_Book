package com.example.dmart.config;

import com.example.dmart.model.Product;
import com.example.dmart.model.User;
import com.example.dmart.repository.ProductRepository;
import com.example.dmart.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(ProductRepository productRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            // Fruits & Vegetables
            productRepository.save(new Product("Banana", "Fruits & Vegetables", 45.0, "1 dozen", 40, "🍌"));
            productRepository.save(new Product("Tomato", "Fruits & Vegetables", 30.0, "1 kg", 60, "🍅"));
            productRepository.save(new Product("Onion", "Fruits & Vegetables", 35.0, "1 kg", 80, "🧅"));
            productRepository.save(new Product("Apple", "Fruits & Vegetables", 180.0, "1 kg", 25, "🍎"));

            // Dairy & Eggs
            productRepository.save(new Product("Toned Milk", "Dairy & Eggs", 28.0, "500 ml", 50, "🥛"));
            productRepository.save(new Product("Paneer", "Dairy & Eggs", 90.0, "200 g", 20, "🧀"));
            productRepository.save(new Product("Eggs", "Dairy & Eggs", 70.0, "1 dozen", 35, "🥚"));

            // Snacks
            productRepository.save(new Product("Potato Chips", "Snacks", 20.0, "1 pack", 100, "🍟"));
            productRepository.save(new Product("Biscuits", "Snacks", 30.0, "1 pack", 0, "🍪"));

            // Beverages
            productRepository.save(new Product("Orange Juice", "Beverages", 110.0, "1 L", 15, "🧃"));
            productRepository.save(new Product("Mineral Water", "Beverages", 20.0, "1 L", 200, "💧"));

            // Staples
            productRepository.save(new Product("Basmati Rice", "Staples", 150.0, "5 kg", 30, "🍚"));
            productRepository.save(new Product("Wheat Flour", "Staples", 280.0, "5 kg", 28, "🌾"));
            productRepository.save(new Product("Sunflower Oil", "Staples", 165.0, "1 L", 22, "🫙"));
        }

        if (userRepository.count() == 0) {
            String hashed = passwordEncoder.encode("test1234");
            userRepository.save(new User("Test Shopper", "shopper@example.com", hashed));
            System.out.println("Seeded test login -> email: shopper@example.com | password: test1234");
        }
    }
}
