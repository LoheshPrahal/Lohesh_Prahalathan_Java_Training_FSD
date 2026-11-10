package com.maverickbank.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionDto(
        @NotNull(message = "Account is required")
        Long accountId,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount should be greater than zero")
        @Digits(integer = 13, fraction = 2, message = "Amount can have at most 2 decimal places")
        BigDecimal amount,
        String description
) {
}
