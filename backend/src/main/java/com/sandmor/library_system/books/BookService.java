package com.sandmor.library_system.books;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepo;

    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> findAllBooks() {
        return bookRepo.findAll();
    }

    @Transactional
    public Book addBook(CreateBookRequest request) {
        if (bookRepo.existsByTitleAndAuthor(request.title(), request.author())) {
            throw new DuplicateBookException(request.title(), request.author());
        }

        var book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());

        return bookRepo.save(book);
    }
}