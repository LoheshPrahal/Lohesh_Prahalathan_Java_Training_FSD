package com.maverickbank.service;

import com.maverickbank.dto.response.FinancialReportDto;
import com.maverickbank.dto.response.TransactionRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.LoanApplicationRepository;
import com.maverickbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class ReportService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public FinancialReportDto getFinancialReport() {
        // Log: financial report is generated
        log.info("Generating the financial report");

        return new FinancialReportDto(
                accountRepository.countByAccountStatus(AccountStatus.ACTIVE),
                orZero(accountRepository.sumBalanceByAccountStatus(AccountStatus.ACTIVE)),
                orZero(transactionRepository.sumAmountByTypes(List.of(TransactionType.DEPOSIT))),
                orZero(transactionRepository.sumAmountByTypes(List.of(TransactionType.WITHDRAWAL))),
                orZero(transactionRepository.sumAmountByTypes(List.of(TransactionType.LOAN_DISBURSEMENT))),
                loanApplicationRepository.getLoanApplicationNumbersByStatus()
        );
    }

    public List<TransactionRespDto> getRegulatoryReport(BigDecimal threshold, int page, int size) {
        // Log: regulatory report with the high value transactions is generated
        log.info("Generating the regulatory report for the amount {} and above", threshold);

        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.getHighValueTransactions(threshold, pageable);
    }

    private BigDecimal orZero(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }
}
