package com.maverickbank.controller;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.dto.response.AccountRespDto;
import com.maverickbank.model.Account;
import com.maverickbank.model.Customer;
import com.maverickbank.repository.CustomerRepository;
import com.maverickbank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;


    @PostMapping("/api/account/add")
    public void createAccount(Principal principal, @Valid @RequestBody AccountDto dto){
        accountService.createAccount(principal.getName(), dto);
    }

    @GetMapping("/api/account/my")
    public List<AccountRespDto> getMyAccounts(Principal principal){
        return accountService.getMyAccounts(principal.getName());
    }

    @GetMapping("/api/account/my/{id}")
    public AccountRespDto getMyAccountById(@PathVariable long id, Principal principal){
        return accountService.getMyAccountById(id, principal.getName());
    }

    @GetMapping("/api/account/pending")
    public List<AccountRespDto> getPendingAccounts(){
        return accountService.getPendingAccounts();
    }

    @PutMapping("/api/account/approve/{id}")
    public void approveAccount(@PathVariable long id, Principal principal){
        accountService.approveAccount(id, principal.getName());
    }
}
