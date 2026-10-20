package com.maverickbank.mapper;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.model.Employee;

public class EmployeeMapper {
    public static Employee convertDtoToEmployee(EmployeeDto dto){
        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(dto.email());

        return employee;
    }
}
