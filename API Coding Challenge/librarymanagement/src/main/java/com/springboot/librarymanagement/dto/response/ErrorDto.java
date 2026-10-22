package com.springboot.librarymanagement.dto.response;

import java.time.Instant;

public record ErrorDto(
        String message,
        String comments,
        Instant timestamp
) {
}
