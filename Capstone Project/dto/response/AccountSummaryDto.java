package com.maverickbank.dto.response;


public record AccountSummaryDto(
        Long accountId,
        String accountNumber,
        Long totalInbound,
        Long totalOutbound
) {
}
