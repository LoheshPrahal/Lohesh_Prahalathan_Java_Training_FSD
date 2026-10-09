package com.libraryManagement.dto;

import com.libraryManagement.enums.Genre;
import com.libraryManagement.enums.Status;

public record BookInfoDto(
    long bookId,
    String title,
    Genre genre,
    Status status,
    String authorName
) {
}
