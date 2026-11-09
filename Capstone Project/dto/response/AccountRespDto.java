package com.maverickbank.dto.response;

import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.AccountType;

import java.time.Instant;

public record AccountRespDto(
        Long accountId,
        String accountNumber,
        AccountType accountType,
        double balance,
        AccountStatus accountStatus,
        Instant openedDate,
        String customerName,
        String branchName,
        String branchAddress,
        String ifscCode
) {
}
