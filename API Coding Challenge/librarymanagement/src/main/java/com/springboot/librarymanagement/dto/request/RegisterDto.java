package com.springboot.librarymanagement.dto.request;

import com.springboot.librarymanagement.enums.Role;

public record RegisterDto(
        String username,
        String password,
        Role role
) {
}
