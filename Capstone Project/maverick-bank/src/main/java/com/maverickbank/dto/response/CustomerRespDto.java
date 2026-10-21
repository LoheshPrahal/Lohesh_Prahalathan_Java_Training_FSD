package com.maverickbank.dto.response;

import com.maverickbank.enums.CustomerGender;
import com.maverickbank.enums.UserStatus;

import java.time.LocalDate;

public record CustomerRespDto(
        long customerId,
        String name,
        CustomerGender gender,
        String email,
        String contact,
        String address,
        LocalDate dateOfBirth,
        String aadhaarNumber,
        String panNumber,
        UserStatus status,
        String username
) {
}
