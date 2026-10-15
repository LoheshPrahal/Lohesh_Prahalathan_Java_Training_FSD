package com.maverickbank.service;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.EmployeeMapper;
import com.maverickbank.model.Employee;
import com.maverickbank.model.User;
import com.maverickbank.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserService userService;
    private final EmployeeRepository employeeRepository;

    public void insertEmployee(@Valid EmployeeDto dto) {
        Employee employee = EmployeeMapper.convertDtoToEmployee(dto);
        User user = userService.getUserObject(dto.username(), dto.password(), UserRole.EMPLOYEE);
        employee.setUser(user);
        employeeRepository.save(employee);

    }

    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found!"));
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }
}
