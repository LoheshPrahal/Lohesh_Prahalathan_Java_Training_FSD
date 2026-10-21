package com.maverickbank.dto.response;

import com.maverickbank.enums.RequestStatus;

import java.time.LocalDate;

public record AccountClosureRespDto(
        Long requestId,
        Long accountId,
        String accountNumber,
        String customerName,
        String reason,
        LocalDate requestDate,
        RequestStatus status
) {
}
