package com.maverickbank.dto.response;

import com.maverickbank.enums.LoanStatus;

import java.math.BigDecimal;
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
        LocalDate applicationDate,
        LoanStatus status,
        LocalDate reviewedDate
) {
}
