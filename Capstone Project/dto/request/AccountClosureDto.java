package com.maverickbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountClosureDto(
        @NotBlank(message = "Reason is required")
        @NotNull(message = "Reason is required")
        String reason
) {
}
