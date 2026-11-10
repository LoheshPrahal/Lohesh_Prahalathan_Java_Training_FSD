package com.maverickbank.dto.response;

import com.maverickbank.enums.RequestStatus;

import java.time.Instant;

public record AccountClosureRespDto(
        Long requestId,
        Long accountId,
        String accountNumber,
        String customerName,
        String reason,
        Instant requestDate,
        RequestStatus status
) {
}
