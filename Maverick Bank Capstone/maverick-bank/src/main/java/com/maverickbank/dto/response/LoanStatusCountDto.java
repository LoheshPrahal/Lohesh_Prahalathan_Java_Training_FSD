package com.maverickbank.dto.response;

import com.maverickbank.enums.LoanStatus;

public record LoanStatusCountDto(
        LoanStatus status,
        Long numberOfApplications
) {
}
