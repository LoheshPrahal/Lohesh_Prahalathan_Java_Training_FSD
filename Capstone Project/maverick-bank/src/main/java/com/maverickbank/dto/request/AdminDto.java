package com.maverickbank.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminDto(
        @NotBlank(message = "Name is required")
        @NotNull(message = "Name is required")
        String name,
        @NotBlank(message = "Email is required")
        @NotNull(message = "Email is required")
        @Email(message = "Email seems invalid")
        String email,
        @NotBlank(message = "Username is required")
        @NotNull(message = "Username is required")
        @Size(min = 3, max = 15, message = "Username should be 3 to 15 characters")
        String username,
        @NotBlank(message = "Password is required")
        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password should be at least 8 characters")
        String password
) {
}
