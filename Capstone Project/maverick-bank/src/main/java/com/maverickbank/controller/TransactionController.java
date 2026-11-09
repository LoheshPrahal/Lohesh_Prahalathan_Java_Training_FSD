package com.maverickbank.controller;

import com.maverickbank.dto.request.TransactionDto;
import com.maverickbank.dto.request.TransferDto;
import com.maverickbank.dto.response.AccountSummaryDto;
import com.maverickbank.dto.response.TransactionRespDto;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/api/transaction/deposit")
    public void deposit(Principal principal, @Valid @RequestBody TransactionDto dto){
        transactionService.deposit(principal.getName(), dto);
    }

    @PostMapping("/api/transaction/withdraw")
    public void withdraw(Principal principal, @Valid @RequestBody TransactionDto dto){
        transactionService.withdraw(principal.getName(), dto);
    }

    @PostMapping("/api/transaction/transfer")
    public void transfer(Principal principal, @Valid @RequestBody TransferDto dto){
        transactionService.transfer(principal.getName(), dto);
    }

    @GetMapping("/api/transaction/{accountNumber}")
    public List<TransactionRespDto> getMyTransactionsByAccountNumber(@PathVariable String accountNumber,
                                                      @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                      @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                                      @RequestParam(value = "transactionType", required = false) TransactionType transactionType,
                                                      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(value = "size", required = false, defaultValue = "20") Integer size){
        return transactionService.getTransactionsByAccountDynamically(accountNumber, fromDate, toDate, transactionType, page, size);
    }

    @GetMapping("/api/transaction/all")
    public List<TransactionRespDto> getAllTransactions(@RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                       @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                                       @RequestParam(value = "transactionType", required = false) TransactionType transactionType,
                                                       @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "20") Integer size){
        return transactionService.getAllTransactions(fromDate, toDate, transactionType, page, size);
    }

    @GetMapping("/api/transaction/summary/{accountId}")
    public AccountSummaryDto getAccountSummary(@PathVariable Long accountId){
        return transactionService.getAccountSummary(accountId);
    }

}
