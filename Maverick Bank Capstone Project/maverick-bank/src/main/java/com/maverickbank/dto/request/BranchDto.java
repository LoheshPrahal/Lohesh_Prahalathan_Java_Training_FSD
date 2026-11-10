package com.maverickbank.dto.request;

import com.maverickbank.enums.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BranchDto(
        @NotBlank(message = "Branch name is required")
        @NotNull(message = "Branch name is required")
        String name,
        @NotBlank(message = "Address is required")
        @NotNull(message = "Address is required")
        String address,
        @NotNull(message = "City is required")
        City city,
        @NotBlank(message = "IFSC code is required")
        @NotNull(message = "IFSC code is required")
        @Pattern(regexp = "[A-Z]{4}0[A-Z0-9]{6}", message = "IFSC code seems invalid")
        String ifscCode
) {
}
