package com.maverickbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BeneficiaryDto(
        @NotBlank(message = "Account name is required")
        @NotNull(message = "Account name is required")
        String accountName,
        @NotBlank(message = "Account number is required")
        @NotNull(message = "Account number is required")
        String accountNumber,
        @NotNull(message = "Branch is required")
        Long branchId
) {
}
