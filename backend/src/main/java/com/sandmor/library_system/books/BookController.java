package com.sandmor.library_system.books;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Book> all() {
        return repo.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Book add(@RequestBody @Valid CreateBookRequest request) {
        var book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        return repo.save(book);
    }
}
