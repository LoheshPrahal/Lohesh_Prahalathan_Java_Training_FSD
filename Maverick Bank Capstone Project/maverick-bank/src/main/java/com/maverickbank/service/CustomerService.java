package com.maverickbank.service;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.dto.request.CustomerDto;
import com.maverickbank.dto.request.CustomerUpdateDto;
import com.maverickbank.dto.response.CustomerRespDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.enums.UserStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.CustomerMapper;
import com.maverickbank.model.Customer;
import com.maverickbank.model.User;
import com.maverickbank.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final AccountService accountService;

    public Customer findCustomerByUsername(String name){
        Customer customer = customerRepository.findByUsername(name);
        if(customer == null) throw new ResourceNotFoundException("Customer not found!");
        return customer;
    }


    //Registration is also account creation for the customer.
    @Transactional(rollbackFor = Exception.class)
    public void insertCustomer(@Valid CustomerDto customerDto) {
        Customer customer = CustomerMapper.convertDtoToEntity(customerDto);
        User user = userService.getUserObject(customerDto.username(), customerDto.password(), UserRole.CUSTOMER);
        customer.setUser(user);
        customerRepository.save(customer);
        accountService.createBankAccount(customer, new AccountDto(customerDto.accountType(), customerDto.branchId()));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
    }

    public CustomerRespDto getCustomerById(Long id) {
        return CustomerMapper.convertEntityToDto(findCustomerById(id));
    }

    public List<CustomerRespDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::convertEntityToDto).toList();
    }

    public CustomerRespDto getProfile(String name) {
        return CustomerMapper.convertEntityToDto(findCustomerByUsername(name));
    }

    public void update(Long id, CustomerUpdateDto dto) {
        Customer customer = findCustomerById(id);
        boolean flag = false;

        if(dto.name() != null && !(dto.name().isBlank())){
            customer.setName(dto.name());
            flag = true;
        }

        if(dto.email() != null && !(dto.email().isBlank())){
            customer.setEmail(dto.email());
            flag = true;
        }

        if(dto.contact() != null && !(dto.contact().isBlank())){
            customer.setContact(dto.contact());
            flag = true;
        }

        if(dto.address() != null && !(dto.address().isBlank())){
            customer.setAddress(dto.address());
            flag = true;
        }

        if(!flag) throw new InvalidCallException("Update operation terminated!");

        customerRepository.save(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deactivate(Long id) {
        Customer customer = findCustomerById(id);
        customer.setStatus(UserStatus.INACTIVE);
        userService.deactivateUser(customer.getUser().getId());
        customerRepository.save(customer);
    }
}
