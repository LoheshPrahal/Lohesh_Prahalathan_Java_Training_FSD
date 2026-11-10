package com.maverickbank.dto.request;

import com.maverickbank.enums.AccountType;
import jakarta.validation.constraints.NotNull;

public record AccountDto(
        @NotNull(message = "Account type is required")
        AccountType accountType,
        @NotNull(message = "Branch is required")
        Long branchId
) {
}
