package com.maverickbank.controller;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.model.Account;
import com.maverickbank.model.Customer;
import com.maverickbank.repository.CustomerRepository;
import com.maverickbank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;


    @PostMapping("/api/account/add")
    public void createAccount(String customerUsername, @Valid @RequestBody AccountDto dto){
        accountService.createAccount(customerUsername, dto);
    }
}
