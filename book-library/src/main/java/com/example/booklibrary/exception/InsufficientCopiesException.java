package com.example.booklibrary.exception;

public class InsufficientCopiesException extends RuntimeException {

    public InsufficientCopiesException(String bookTitle, int requested, int available) {
        super(String.format(
            "Cannot purchase %d cop%s of \"%s\" — only %d cop%s available.",
            requested, requested == 1 ? "y" : "ies",
            bookTitle,
            available, available == 1 ? "y" : "ies"
        ));
    }
}
