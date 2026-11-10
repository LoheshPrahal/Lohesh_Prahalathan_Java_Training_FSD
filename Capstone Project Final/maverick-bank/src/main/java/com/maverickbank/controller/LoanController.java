package com.maverickbank.controller;

import com.maverickbank.dto.request.LoanDto;
import com.maverickbank.model.Loan;
import com.maverickbank.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/api/loan/add")
    public void addLoan(@Valid @RequestBody LoanDto dto){
        loanService.addLoan(dto);
    }

    @GetMapping("/api/loan/all")
    public List<Loan> getAllLoans(){
        return loanService.getAllLoans();
    }
}
