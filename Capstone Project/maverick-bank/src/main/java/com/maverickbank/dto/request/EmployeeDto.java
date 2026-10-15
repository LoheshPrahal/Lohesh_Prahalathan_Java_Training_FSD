package com.maverickbank.dto.request;

public record EmployeeDto(
        String name,
        String email,
        String username,
        String password
) {
}
