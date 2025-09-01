package com.sandmor.library_system.loans;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandmor.library_system.books.BookRepository;
import com.sandmor.library_system.users.UserRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public LoanController(LoanRepository loanRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/{bookId}")
    @Transactional
    public ResponseEntity<?> borrow(@PathVariable Long bookId, Principal principal) {
        var user = userRepo.findByEmail(principal.getName()).get();
        var book = bookRepo.findById(bookId).orElseThrow();
        if (!book.isAvailable())
            return ResponseEntity.badRequest().body("Book is not available");
        book.setAvailable(false);

        var loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loanRepo.save(loan);
        return ResponseEntity.ok(loan);
    }
}
