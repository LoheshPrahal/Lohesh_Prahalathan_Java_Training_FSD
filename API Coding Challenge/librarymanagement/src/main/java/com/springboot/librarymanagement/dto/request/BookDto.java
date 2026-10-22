package com.springboot.librarymanagement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDto(

        String title,

        String author,
        @Column(length = 13)

        String isbn,

        int publicationYear
) {
}
