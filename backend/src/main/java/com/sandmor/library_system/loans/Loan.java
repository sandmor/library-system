package com.sandmor.library_system.loans;

import java.time.LocalDate;

import com.sandmor.library_system.books.Book;
import com.sandmor.library_system.users.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Book book;
    private LocalDate loanDate;
    private LocalDate returnDate;
}
