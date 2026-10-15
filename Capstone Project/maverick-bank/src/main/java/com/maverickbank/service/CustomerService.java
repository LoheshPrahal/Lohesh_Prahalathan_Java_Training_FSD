package com.maverickbank.service;

import com.maverickbank.dto.request.CustomerDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.CustomerMapper;
import com.maverickbank.model.Customer;
import com.maverickbank.model.User;
import com.maverickbank.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    public void insertCustomer(@Valid CustomerDto customerDto) {
        Customer customer = CustomerMapper.convertDtoToEntity(customerDto);

        User user = userService.getUserObject(customerDto.username(), customerDto.password(), UserRole.CUSTOMER);
        customer.setUser(user);

        customerRepository.save(customer);
    }

    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
