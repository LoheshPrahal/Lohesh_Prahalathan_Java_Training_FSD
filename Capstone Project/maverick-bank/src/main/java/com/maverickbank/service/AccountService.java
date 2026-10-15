package com.maverickbank.service;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.AccountMapper;
import com.maverickbank.model.Account;
import com.maverickbank.model.Customer;
import com.maverickbank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final CustomerRepository customerRepository;

    public Customer findByUsername(String customerUsername) {
        return customerRepository.findByUsername(customerUsername);
    }

    public void createAccount(String customerUsername, AccountDto dto) {
        Customer customer = findByUsername(customerUsername);
        Account account = AccountMapper.convertDtoToEntity(dto);
        account.setCustomer(customer);
    }
}
