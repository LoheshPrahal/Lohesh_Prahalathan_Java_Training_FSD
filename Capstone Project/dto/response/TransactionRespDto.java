package com.maverickbank.dto.response;

import com.maverickbank.enums.TransactionStatus;
import com.maverickbank.enums.TransactionType;

import java.time.LocalDateTime;

public record TransactionRespDto(
        Long transactionId,
        String accountNumber,
        TransactionType transactionType,
        double amount,
        LocalDateTime transactionDate,
        String description,
        TransactionStatus transactionStatus
) {
}
