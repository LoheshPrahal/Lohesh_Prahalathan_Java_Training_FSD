package com.maverickbank.controller;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.dto.response.AccountRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.AccountType;
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
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;


    @PostMapping("/api/account/add")
    public void createAccount(Principal principal, @Valid @RequestBody AccountDto dto){
        accountService.createAccount(principal.getName(), dto);
    }

    // Filter - accountType and accountStatus are optional
    @GetMapping("/api/account/my")
    public List<AccountRespDto> getMyAccounts(Principal principal,
                                              @RequestParam(value = "accountType", required = false) AccountType accountType,
                                              @RequestParam(value = "accountStatus", required = false) AccountStatus accountStatus){
        return accountService.getMyAccounts(principal.getName(), accountType, accountStatus);
    }

    @GetMapping("/api/account/my/{id}")
    public AccountRespDto getMyAccountById(@PathVariable Long id, Principal principal){
        return accountService.getMyAccountById(id, principal.getName());
    }

    // Bank employee - details of any account
    @GetMapping("/api/account/details/{id}")
    public AccountRespDto getAccountDetails(@PathVariable Long id){
        return accountService.getAccountDetails(id);
    }

    // Bank employee - find account with the account number
    @GetMapping("/api/account/number/{accountNumber}")
    public AccountRespDto getAccountDetailsByNumber(@PathVariable String accountNumber){
        return accountService.getAccountDetailsByNumber(accountNumber);
    }

    // Filter - accountType and branchId are optional
    @GetMapping("/api/account/pending")
    public List<AccountRespDto> getPendingAccounts(@RequestParam(value = "accountType", required = false) AccountType accountType,
                                                   @RequestParam(value = "branchId", required = false) Long branchId){
        return accountService.getPendingAccounts(accountType, branchId);
    }

    @PutMapping("/api/account/approve/{id}")
    public void approveAccount(@PathVariable Long id, Principal principal){
        accountService.approveAccount(id, principal.getName());
    }
}
