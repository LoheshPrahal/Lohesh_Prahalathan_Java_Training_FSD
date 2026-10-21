package com.maverickbank.dto.request;

import com.maverickbank.enums.AccountType;
import com.maverickbank.enums.CustomerGender;
import com.maverickbank.enums.UserStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CustomerDto(
        @NotBlank(message = "Name is required")
        @NotNull(message = "Name is required")
        String name,
        @NotNull(message = "Gender is required")
        CustomerGender gender,
        @NotBlank(message = "Email is required")
        @NotNull(message = "Email is required")
        @Email(message = "Email seems invalid")
        String email,
        @NotBlank(message = "Contact is required")
        @NotNull(message = "Contact is required")
        @Pattern(regexp = "\\d{10}", message = "contact should be 10 digit mobile number")
        String contact,
        @NotBlank(message = "Address is required")
        @NotNull(message = "Address is required")
        String address,
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        @NotBlank(message = "Aadhaar number is required")
        @NotNull(message = "Aadhaar number is required")
        @Pattern(regexp = "\\d{12}", message = "Aadhaar number should be 12 digits")
        String aadhaarNumber,
        @NotBlank(message = "PAN number is required")
        @NotNull(message = "PAN number is required")
        @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]", message = "PAN number seems invalid")
        String panNumber,
        @NotBlank(message = "Username is required")
        @NotNull(message = "Username is required")
        @Size(min = 3, max = 15, message = "Username should be 3 to 15 characters")
        String username,
        @NotBlank(message = "Password is required")
        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password should be at least 8 characters")
        String password,
        @NotNull(message = "Account type is required")
        AccountType accountType,
        @NotNull(message = "Branch is required")
        Long branchId
) {
}
