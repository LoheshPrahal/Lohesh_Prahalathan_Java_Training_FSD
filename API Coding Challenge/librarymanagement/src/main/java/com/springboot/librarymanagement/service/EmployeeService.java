package com.springboot.librarymanagement.service;

import com.springboot.librarymanagement.dto.response.EmployeeRespDto;
import com.springboot.librarymanagement.enums.JobTitle;
import com.springboot.librarymanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public List<EmployeeRespDto> getEmployeesByJobTitleAndCompanyName(JobTitle jobTitle, String companyName, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.getEmployeesByJobTitleAndCompanyName(jobTitle, companyName, pageable);
    }
}
