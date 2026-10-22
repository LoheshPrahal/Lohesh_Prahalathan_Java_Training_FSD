package com.springboot.librarymanagement.controller;

import com.springboot.librarymanagement.dto.response.EmployeeRespDto;
import com.springboot.librarymanagement.enums.JobTitle;
import com.springboot.librarymanagement.model.Employee;
import com.springboot.librarymanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;



    @GetMapping("/api/employee/fetch")
    public List<EmployeeRespDto> getEmployeesByJobTitleAndCompanyName(@RequestParam JobTitle jobTitle, @RequestParam String companyName, @RequestParam Integer page, @RequestParam Integer size){
        return employeeService.getEmployeesByJobTitleAndCompanyName(jobTitle, companyName, page, size);
    }
}
