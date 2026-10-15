package com.maverickbank.dto.request;

import com.maverickbank.enums.AccountType;

public record AccountDto(
        AccountType accountType
) {
}
