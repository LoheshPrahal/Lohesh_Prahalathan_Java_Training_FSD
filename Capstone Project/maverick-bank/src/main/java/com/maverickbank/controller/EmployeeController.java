package com.maverickbank.controller;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.mapper.EmployeeMapper;
import com.maverickbank.model.Employee;
import com.maverickbank.model.User;
import com.maverickbank.service.EmployeeService;
import com.maverickbank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final UserService userService;
    private final EmployeeService employeeService;

    @PostMapping("/api/employee/add")
    public void insertEmployee(@Valid @RequestBody EmployeeDto dto){
        employeeService.insertEmployee(dto);
    }

    @GetMapping("/api/employee/{id}")
    public Employee getEmployeeById(@PathVariable long id){
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/api/employee/all")
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
}
