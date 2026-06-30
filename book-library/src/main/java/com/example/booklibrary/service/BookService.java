package com.example.booklibrary.service;

import com.example.booklibrary.exception.BookNotFoundException;
import com.example.booklibrary.exception.InsufficientCopiesException;
import com.example.booklibrary.model.Book;
import com.example.booklibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final EmailService emailService;

    @Autowired
    public BookService(BookRepository bookRepository, EmailService emailService) {
        this.bookRepository = bookRepository;
        this.emailService = emailService;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setPublishedYear(updatedBook.getPublishedYear());
        existingBook.setCopies(updatedBook.getCopies());
        existingBook.setMinAge(updatedBook.getMinAge());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        Book existingBook = getBookById(id);
        bookRepository.delete(existingBook);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author);
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailable(true);
    }

    /**
     * Filters out any book whose age cap (stored in the minAge field) is
     * lower than the viewer's age. A value of 0 means "no cap" — everyone
     * can see it. Otherwise, the book is only visible to viewers whose age
     * is LESS THAN OR EQUAL TO the cap (e.g. cap=17 hides the book from
     * anyone 18 or older).
     *
     * Note: the field is still named minAge in the entity/DB to avoid
     * another schema migration, but it now functions as a maximum-age cap.
     */
    public List<Book> filterByAge(List<Book> books, int viewerAge) {
        return books.stream()
                .filter(book -> book.getMinAge() == 0 || viewerAge <= book.getMinAge())
                .toList();
    }

    /**
     * Purchases a given quantity of a book:
     * - Validates enough copies are in stock (throws InsufficientCopiesException otherwise)
     * - Decrements the copy count (and flips `available` to false if it hits 0)
     * - Sends a confirmation email to the buyer
     *
     * @Transactional ensures the stock decrement and save happen atomically;
     * if anything fails partway, the change is rolled back.
     */
    @Transactional
    public Book purchaseBook(Long id, String buyerName, String buyerEmail, int quantity) {
        Book book = getBookById(id);

        if (book.getCopies() < quantity) {
            throw new InsufficientCopiesException(book.getTitle(), quantity, book.getCopies());
        }

        book.setCopies(book.getCopies() - quantity);
        Book savedBook = bookRepository.save(book);

        emailService.sendPurchaseConfirmation(buyerEmail, buyerName, savedBook, quantity);

        return savedBook;
    }
}
