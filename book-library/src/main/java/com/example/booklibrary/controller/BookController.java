package com.example.booklibrary.controller;

import com.example.booklibrary.dto.PurchaseRequest;
import com.example.booklibrary.model.Book;
import com.example.booklibrary.model.User;
import com.example.booklibrary.repository.UserRepository;
import com.example.booklibrary.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@Tag(name = "Books", description = "Endpoints for managing the book library")
public class BookController {

    private final BookService bookService;
    private final UserRepository userRepository;

    @Autowired
    public BookController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    /**
     * Looks up the logged-in user's age from their authenticated email.
     * Returns Integer.MAX_VALUE if somehow no user is found, so filtering
     * fails open to "show nothing restricted is hidden" only in a bug
     * scenario — in normal operation, Spring Security guarantees a real
     * logged-in user reaches this point since /api/books requires auth.
     */
    private int currentUserAge(Authentication authentication) {
        if (authentication == null) {
            return 0; // No logged-in user: treat as most restricted (shouldn't happen, endpoint requires auth)
        }
        return userRepository.findByEmail(authentication.getName())
                .map(User::getAge)
                .orElse(0);
    }

    @Operation(summary = "Get all books", description = "Returns all books the logged-in user is old enough to see, optionally filtered by author, genre, or availability")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @Parameter(description = "Filter by author name") @RequestParam(required = false) String author,
            @Parameter(description = "Filter by genre") @RequestParam(required = false) String genre,
            @Parameter(description = "Filter to only available books") @RequestParam(required = false) Boolean available,
            Authentication authentication) {

        int viewerAge = currentUserAge(authentication);
        List<Book> books;

        if (author != null) {
            books = bookService.getBooksByAuthor(author);
        } else if (genre != null) {
            books = bookService.getBooksByGenre(genre);
        } else if (available != null && available) {
            books = bookService.getAvailableBooks();
        } else {
            books = bookService.getAllBooks();
        }

        return ResponseEntity.ok(bookService.filterByAge(books, viewerAge));
    }

    @Operation(summary = "Get a book by id")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Create a new book")
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Update an existing book")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @Operation(summary = "Delete a book")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Purchase copies of a book", description = "Reduces stock by the given quantity and emails a confirmation to the buyer")
    @PostMapping("/{id}/purchase")
    public ResponseEntity<Book> purchaseBook(@PathVariable Long id, @Valid @RequestBody PurchaseRequest request) {
        Book updatedBook = bookService.purchaseBook(
                id, request.getBuyerName(), request.getBuyerEmail(), request.getQuantity()
        );
        return ResponseEntity.ok(updatedBook);
    }
}
