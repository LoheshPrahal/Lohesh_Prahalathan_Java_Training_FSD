package com.maverickbank.controller;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.dto.request.EmployeeUpdateDto;
import com.maverickbank.dto.response.EmployeeRespDto;
import com.maverickbank.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/api/employee/add")
    public void insertEmployee(@Valid @RequestBody EmployeeDto dto){
        employeeService.insertEmployee(dto);
    }

    @GetMapping("/api/employee/{id}")
    public EmployeeRespDto getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/api/employee/all")
    public List<EmployeeRespDto> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PutMapping("/api/employee/{id}")
    public void update(@PathVariable Long id, @RequestBody EmployeeUpdateDto dto){
        employeeService.update(id, dto);
    }

    @PutMapping("/api/employee/deactivate/{id}")
    public void deactivate(@PathVariable Long id){
        employeeService.deactivate(id);
    }
}
