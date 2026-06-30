package com.example.booklibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 0, message = "Published year must be a positive number")
    private int publishedYear;

    @Min(value = 0, message = "Copies cannot be negative")
    private int copies = 1;

    // Derived from copies > 0 on every read/write — kept as a real column
    // so existing filters (?available=true) and the JSON shape don't break.
    private boolean available = true;

    // Minimum age a logged-in reader must be to see this book at all.
    // 0 means no restriction. Set by the owner when adding/editing a book.
    @Min(value = 0, message = "Minimum age cannot be negative")
    private int minAge = 0;

    public Book() {
    }

    public Book(String title, String author, String genre, int publishedYear, int copies) {
        this(title, author, genre, publishedYear, copies, 0);
    }

    public Book(String title, String author, String genre, int publishedYear, int copies, int minAge) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishedYear = publishedYear;
        this.copies = copies;
        this.available = copies > 0;
        this.minAge = minAge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
        this.available = copies > 0;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }
}
