package com.maverickbank.controller;


import com.maverickbank.dto.response.FinancialReportDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.LoanApplicationRepository;
import com.maverickbank.repository.TransactionRepository;
import com.maverickbank.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/api/report/financial")
    public FinancialReportDto getFinancialReport(){
        return reportService.getFinancialReport();
    }


}
