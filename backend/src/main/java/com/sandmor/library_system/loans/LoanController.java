package com.sandmor.library_system.loans;

import com.sandmor.library_system.common.annotations.ApiRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@ApiRestController("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Loan> borrow(@PathVariable Long bookId, Principal principal) {
        Loan newLoan = loanService.borrowBook(bookId, principal.getName());
        return ResponseEntity.ok(newLoan);
    }
}