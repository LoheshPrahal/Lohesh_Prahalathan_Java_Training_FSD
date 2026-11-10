package com.maverickbank.controller;

import com.maverickbank.dto.request.TransactionDto;
import com.maverickbank.dto.request.TransferDto;
import com.maverickbank.dto.response.AccountSummaryDto;
import com.maverickbank.dto.response.TransactionRespDto;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/api/transaction/my/{accountNumber}")
    public List<TransactionRespDto> getMyTransactionsByAccountNumber(@PathVariable String accountNumber,
                                                                     @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                                     @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                                                     @RequestParam(value = "transactionType", required = false) TransactionType transactionType,
                                                                     @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                     @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                                                     Principal principal){
        return transactionService.getTransactionsByAccountDynamicFilter(principal.getName(), accountNumber, fromDate, toDate, transactionType, page, size);

    }

    @GetMapping("/api/transaction/{accountNumber}")
    public List<TransactionRespDto> getTransactionsByAccountNumber(@PathVariable String accountNumber,
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

    // Statement - the customer downloads the statement of an own account as a csv file
    @GetMapping("/api/transaction/statement/{accountNumber}")
    public ResponseEntity<String> getStatement(@PathVariable String accountNumber,
                                               @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                               @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                               Principal principal){
        String csv = transactionService.getStatementCsv(principal.getName(), accountNumber, fromDate, toDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"statement-" + accountNumber + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    @GetMapping("/api/transaction/summary/{accountId}")
    public AccountSummaryDto getAccountSummary(@PathVariable Long accountId){
        return transactionService.getAccountSummary(accountId);
    }
}
