package com.maverickbank.dto.response;

import com.maverickbank.enums.UserStatus;

public record EmployeeRespDto(
        long employeeId,
        String name,
        String email,
        UserStatus status,
        String username,
        long branchId,
        String branchName
) {
}
