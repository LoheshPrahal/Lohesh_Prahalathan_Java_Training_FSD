package com.maverickbank.service;

import com.maverickbank.dto.request.EmployeeDto;
import com.maverickbank.dto.request.EmployeeUpdateDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.enums.UserStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Branch;
import com.maverickbank.model.Employee;
import com.maverickbank.model.User;
import com.maverickbank.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private UserService userService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private BranchService branchService;

    @Test
    public void insertEmployeeTest() {
        EmployeeDto dto = new EmployeeDto("John", "john@gmail.com", "john", "password123", 1L);
        Branch branch = new Branch();
        branch.setId(1L);
        when(branchService.getBranchById(1L)).thenReturn(branch);
        when(userService.getUserObject("john", "password123", UserRole.EMPLOYEE)).thenReturn(new User());

        employeeService.insertEmployee(dto);

        verify(branchService, times(1)).getBranchById(1L);
        verify(userService, times(1)).getUserObject("john", "password123", UserRole.EMPLOYEE);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void getEmployeeByIdTest() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John");
        employee.setUser(new User());
        employee.setBranch(new Branch());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertEquals("John", employeeService.getEmployeeById(1L).name());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void getEmployeeByIdTest_Invalid() {
        when(employeeRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(5L));
        verify(employeeRepository, times(1)).findById(5L);
    }

    @Test
    public void getAllEmployeesTest() {
        Employee employee = new Employee();
        employee.setUser(new User());
        employee.setBranch(new Branch());
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        assertEquals(1, employeeService.getAllEmployees().size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void updateTest() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        EmployeeUpdateDto dto = new EmployeeUpdateDto("New Name", "new@gmail.com");

        employeeService.update(1L, dto);

        assertEquals("New Name", employee.getName());
        assertEquals("new@gmail.com", employee.getEmail());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void updateTest_Invalid() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
        EmployeeUpdateDto dto = new EmployeeUpdateDto("", null);

        assertThrows(InvalidCallException.class, () -> employeeService.update(1L, dto));
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    public void deactivateTest() {
        Employee employee = new Employee();
        User user = new User();
        user.setId(5L);
        employee.setUser(user);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deactivate(1L);

        assertEquals(UserStatus.INACTIVE, employee.getStatus());
        verify(userService, times(1)).deactivateUser(5L);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void findByUsernameTest() {
        Employee employee = new Employee();
        when(employeeRepository.findByUsername("john")).thenReturn(employee);

        assertEquals(employee, employeeService.findByUsername("john"));
        verify(employeeRepository, times(1)).findByUsername("john");
    }

    @Test
    public void findByUsernameTest_Invalid() {
        when(employeeRepository.findByUsername("missing")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.findByUsername("missing"));
        verify(employeeRepository, times(1)).findByUsername("missing");
    }
}
