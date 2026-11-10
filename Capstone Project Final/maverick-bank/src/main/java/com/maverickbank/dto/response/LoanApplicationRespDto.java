package com.maverickbank.dto.response;

import com.maverickbank.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record LoanApplicationRespDto(
        Long applicationId,
        String loanType,
        Long accountId,
        String accountNumber,
        String customerName,
        BigDecimal requestedAmount,
        Double interestRate,
        Integer tenureMonths,
        String purpose,
        Instant applicationDate,
        LoanStatus status,
        LocalDate reviewedDate,
        // Repayment - the amount still to be paid
        BigDecimal remainingAmount
) {
}
