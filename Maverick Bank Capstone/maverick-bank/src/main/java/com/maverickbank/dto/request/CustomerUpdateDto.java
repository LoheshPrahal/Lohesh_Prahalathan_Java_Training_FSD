package com.maverickbank.dto.request;

public record CustomerUpdateDto(
        String name,
        String email,
        String contact,
        String address
) {
}
