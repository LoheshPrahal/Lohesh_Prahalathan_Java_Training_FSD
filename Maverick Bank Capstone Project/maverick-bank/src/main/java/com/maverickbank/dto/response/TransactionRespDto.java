package com.maverickbank.dto.response;

import com.maverickbank.enums.TransactionStatus;
import com.maverickbank.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionRespDto(
        Long transactionId,
        String accountNumber,
        TransactionType transactionType,
        BigDecimal amount,
        Instant transactionDate,
        String description,
        TransactionStatus transactionStatus
) {
}
