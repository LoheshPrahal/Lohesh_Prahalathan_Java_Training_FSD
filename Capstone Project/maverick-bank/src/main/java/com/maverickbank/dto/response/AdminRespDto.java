package com.maverickbank.dto.response;

import com.maverickbank.enums.UserStatus;

public record AdminRespDto(
        long adminId,
        String name,
        String email,
        UserStatus status,
        String username
) {
}