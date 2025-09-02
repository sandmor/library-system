package com.sandmor.library_system.books;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sandmor.library_system.common.annotations.ApiRestController;

import jakarta.validation.Valid;

@ApiRestController("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> all() {
        return bookService.findAllBooks();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> add(@RequestBody @Valid CreateBookRequest request) {
        Book newBook = bookService.addBook(request);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }
}
