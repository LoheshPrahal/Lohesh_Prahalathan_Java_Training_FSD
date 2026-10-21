package com.maverickbank.service;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.dto.request.EmployeeUpdateDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.enums.UserStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.EmployeeMapper;
import com.maverickbank.model.Employee;
import com.maverickbank.model.User;
import com.maverickbank.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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

    public void update(long id, EmployeeUpdateDto dto) {
        Employee employee = getEmployeeById(id);
        boolean flag = false;

        if(dto.name() != null && !(dto.name().isBlank())){
            employee.setName(dto.name());
            flag = true;
        }

        if(dto.email() != null && !(dto.email().isBlank())){
            employee.setEmail(dto.email());
            flag = true;
        }

        if(!flag){
            throw new InvalidCallException("Update operation terminated!");
        }

        employeeRepository.save(employee);
    }

    public void deactivate(long id) {
        Employee employee = getEmployeeById(id);
        employee.setStatus(UserStatus.INACTIVE);
        userService.deactivateUser(employee.getUser().getId());
        employeeRepository.save(employee);
    }

    public Employee findByUsername(String name){
        Employee employee = employeeRepository.findByUsername(name);
        if(employee == null) throw new ResourceNotFoundException("Employee not found!");

        return employee;
    }
}
