package com.example.booklibrary.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("An account with email \"" + email + "\" already exists.");
    }
}
