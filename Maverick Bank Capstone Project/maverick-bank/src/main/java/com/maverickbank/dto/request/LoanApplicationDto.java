package com.maverickbank.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record LoanApplicationDto(
        @NotNull(message = "Loan is required")
        Long loanId,
        @NotNull(message = "Account is required")
        Long accountId,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount should be greater than zero")
        @Digits(integer = 13, fraction = 2, message = "Amount can have at most 2 decimal places")
        BigDecimal requestedAmount,
        @NotBlank(message = "Purpose is required")
        @NotNull(message = "Purpose is required")
        String purpose
) {
}
