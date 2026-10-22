package com.springboot.librarymanagement.dto.response;

import com.springboot.librarymanagement.enums.JobTitle;
import com.springboot.librarymanagement.enums.Status;

public record EmployeeRespDto(
        String employeeName,
        JobTitle jobTitle,
        String companyName,
        Status status
) {
}
