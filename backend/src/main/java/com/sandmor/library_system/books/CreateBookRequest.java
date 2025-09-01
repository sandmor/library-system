package com.sandmor.library_system.books;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBookRequest(
        @NotBlank(message = "Title is required") @Size(min = 2, max = 256, message = "Title must be between 2 and 256 characters") String title,
        @NotBlank(message = "Author is required") @Size(min = 2, max = 256, message = "Author must be between 2 and 256 characters") String author) {
}
