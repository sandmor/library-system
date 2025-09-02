package com.sandmor.library_system.books;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateBookException extends RuntimeException {
    public DuplicateBookException(String title, String author) {
        super("Book already exists: " + title + " by " + author);
    }
}
