package com.sandmor.library_system.loans;

import com.sandmor.library_system.books.BookRepository;
import com.sandmor.library_system.users.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public LoanService(LoanRepository loanRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Loan borrowBook(Long bookId, String userEmail) {
        var user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));

        var book = bookRepo.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book '" + book.getTitle() + "' is already on loan.");
        }

        book.setAvailable(false);

        var loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());

        return loanRepo.save(loan);
    }
}