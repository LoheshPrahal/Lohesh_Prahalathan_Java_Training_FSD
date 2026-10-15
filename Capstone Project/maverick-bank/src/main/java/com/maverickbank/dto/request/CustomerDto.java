package com.maverickbank.dto.request;

import com.maverickbank.enums.CustomerGender;
import com.maverickbank.enums.UserStatus;

import java.time.LocalDate;

public record CustomerDto(
    String name,
    CustomerGender gender,
    String email,
    String address,
    LocalDate dateOfBirth,
    String aadhaarNumber,
    String username,
    String password
) {
}
