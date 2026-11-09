package com.maverickbank.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record TransactionDto(
        @NotNull(message = "Account is required")
        Long accountId,
        @Positive(message = "Amount should be greater than zero")
        @Digits(integer = 13, fraction = 2, message = "Amount can have at most 2 decimal places")
        double amount,
        String description
) {
}
