package com.maverickbank.service;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.dto.request.CustomerDto;
import com.maverickbank.dto.request.CustomerUpdateDto;
import com.maverickbank.enums.AccountType;
import com.maverickbank.enums.CustomerGender;
import com.maverickbank.enums.UserRole;
import com.maverickbank.enums.UserStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Customer;
import com.maverickbank.model.User;
import com.maverickbank.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserService userService;
    @Mock
    private AccountService accountService;

    @Test
    public void findCustomerByUsernameTest() {
        Customer customer = new Customer();
        when(customerRepository.findByUsername("john")).thenReturn(customer);

        assertEquals(customer, customerService.findCustomerByUsername("john"));
        verify(customerRepository, times(1)).findByUsername("john");
    }

    @Test
    public void findCustomerByUsernameTest_Invalid() {
        when(customerRepository.findByUsername("missing")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> customerService.findCustomerByUsername("missing"));
        verify(customerRepository, times(1)).findByUsername("missing");
    }

    @Test
    public void insertCustomerTest() {
        CustomerDto dto = new CustomerDto("John", CustomerGender.MALE, "john@gmail.com", "9876543210",
                "Chennai", LocalDate.of(2000, 1, 1), "123456789012", "ABCDE1234F",
                "john", "password123", AccountType.SAVINGS, 1L);
        User user = new User();
        when(userService.getUserObject("john", "password123", UserRole.CUSTOMER)).thenReturn(user);

        customerService.insertCustomer(dto);

        verify(userService, times(1)).getUserObject("john", "password123", UserRole.CUSTOMER);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(accountService, times(1)).createBankAccount(any(Customer.class),
                eq(new AccountDto(AccountType.SAVINGS, 1L)));
    }

    @Test
    public void getCustomerByIdTest() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setDateOfBirth(LocalDate.of(2000, 1, 1));
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertEquals("John", customerService.getCustomerById(1L).name());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void getCustomerByIdTest_Invalid() {
        when(customerRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(5L));
        verify(customerRepository, times(1)).findById(5L);
    }

    @Test
    public void getAllCustomersTest() {
        Customer customer = new Customer();
        customer.setDateOfBirth(LocalDate.of(2000, 1, 1));
        customer.setUser(new User());
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        assertEquals(1, customerService.getAllCustomers().size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void getProfileTest() {
        Customer customer = new Customer();
        customer.setName("John");
        customer.setDateOfBirth(LocalDate.of(2000, 1, 1));
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        when(customerRepository.findByUsername("john")).thenReturn(customer);

        assertEquals("john", customerService.getProfile("john").username());
        verify(customerRepository, times(1)).findByUsername("john");
    }

    @Test
    public void updateTest() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerUpdateDto dto = new CustomerUpdateDto("New Name", "new@gmail.com", null, null);

        customerService.update(1L, dto);

        assertEquals("New Name", customer.getName());
        assertEquals("new@gmail.com", customer.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void updateTest_Invalid() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerUpdateDto dto = new CustomerUpdateDto(null, "", null, null);

        assertThrows(InvalidCallException.class, () -> customerService.update(1L, dto));
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void deactivateTest() {
        Customer customer = new Customer();
        User user = new User();
        user.setId(5L);
        customer.setUser(user);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deactivate(1L);

        assertEquals(UserStatus.INACTIVE, customer.getStatus());
        verify(userService, times(1)).deactivateUser(5L);
        verify(customerRepository, times(1)).save(customer);
    }
}
