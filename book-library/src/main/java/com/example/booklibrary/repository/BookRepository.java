package com.example.booklibrary.repository;

import com.example.booklibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorIgnoreCase(String author);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByAvailable(boolean available);
}
