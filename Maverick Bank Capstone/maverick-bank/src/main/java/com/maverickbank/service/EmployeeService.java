package com.maverickbank.service;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.dto.request.EmployeeUpdateDto;
import com.maverickbank.dto.response.EmployeeRespDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.enums.UserStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.EmployeeMapper;
import com.maverickbank.model.Branch;
import com.maverickbank.model.Employee;
import com.maverickbank.model.User;
import com.maverickbank.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class EmployeeService {
    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final BranchService branchService;

    public void insertEmployee(@Valid EmployeeDto dto) {
        Branch branch = branchService.getBranchById(dto.branchId());

        Employee employee = EmployeeMapper.convertDtoToEntity(dto);
        User user = userService.getUserObject(dto.username(), dto.password(), UserRole.EMPLOYEE);
        employee.setUser(user);
        employee.setBranch(branch);
        employeeRepository.save(employee);

        // Log: a new bank employee is added
        log.info("Employee {} is added", dto.username());
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found!"));
    }

    public EmployeeRespDto getEmployeeById(Long id) {
        // Log: details of an employee are fetched
        log.info("Fetching employee id {}", id);

        return EmployeeMapper.convertEntityToDto(findEmployeeById(id));
    }

    public List<EmployeeRespDto> getAllEmployees(){
        // Log: all the employees are listed
        log.info("Fetching all the employees");

        return employeeRepository.findAll().stream().map(EmployeeMapper::convertEntityToDto).toList();
    }

    public void update(Long id, EmployeeUpdateDto dto) {
        Employee employee = findEmployeeById(id);
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

        // Log: details of the employee are updated
        log.info("Employee id {} is updated", id);
    }

    public void deactivate(Long id) {
        Employee employee = findEmployeeById(id);
        employee.setStatus(UserStatus.INACTIVE);
        userService.deactivateUser(employee.getUser().getId());
        employeeRepository.save(employee);

        // Log: the employee cannot login anymore
        log.info("Employee id {} is deactivated", id);
    }

    public Employee findByUsername(String name){
        Employee employee = employeeRepository.findByUsername(name);
        if(employee == null) throw new ResourceNotFoundException("Employee not found!");
        return employee;
    }
}
