package com.maverickbank.dto.response;

import java.math.BigDecimal;

public record AccountSummaryDto(
        long accountId,
        String accountNumber,
        BigDecimal totalInbound,
        BigDecimal totalOutbound
) {
}
