package com.maverickbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record LoanDto(
        @NotBlank(message = "Loan type is required")
        @NotNull(message = "Loan type is required")
        String loanType,
        @NotNull(message = "Minimum amount is required")
        @Positive(message = "Minimum amount should be greater than zero")
        double minAmount,
        @NotNull(message = "Maximum amount is required")
        @Positive(message = "Maximum amount should be greater than zero")
        double maxAmount,
        @NotNull(message = "Interest rate is required")
        @Positive(message = "Interest rate should be greater than zero")
        Double interestRate,
        @NotNull(message = "Tenure is required")
        @Positive(message = "Tenure should be greater than zero")
        Integer tenureMonths
) {
}
