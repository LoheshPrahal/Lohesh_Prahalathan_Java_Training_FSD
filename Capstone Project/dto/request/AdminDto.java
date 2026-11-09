package com.maverickbank.dto.request;

public record AdminDto(
        String name,
        String email,
        String username,
        String password
) {
}
