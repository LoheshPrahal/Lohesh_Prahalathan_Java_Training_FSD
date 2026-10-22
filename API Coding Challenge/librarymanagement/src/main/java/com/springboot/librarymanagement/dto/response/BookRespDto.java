package com.springboot.librarymanagement.dto.response;


public record BookRespDto(
        long id,
        String title,
        String authorName,
        String isbn,
        int pubicationYear
) {
}
