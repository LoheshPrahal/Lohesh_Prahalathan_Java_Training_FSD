package com.maverickbank.mapper;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.dto.response.EmployeeRespDto;
import com.maverickbank.model.Employee;

public class EmployeeMapper {
    public static Employee convertDtoToEntity(EmployeeDto dto){
        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(dto.email());

        return employee;
    }

    public static EmployeeRespDto convertEntityToDto(Employee employee){
        return new EmployeeRespDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getStatus(),
                employee.getUser().getUsername(),
                employee.getBranch().getId(),
                employee.getBranch().getName()
        );
    }
}
