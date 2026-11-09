package com.maverickbank.service;

import com.maverickbank.dto.response.FinancialReportDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.LoanApplicationRepository;
import com.maverickbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public FinancialReportDto getFinancialReport() {
        return new FinancialReportDto(
                accountRepository.countByAccountStatus(AccountStatus.ACTIVE),
                orZero(accountRepository.sumBalanceByAccountStatus(AccountStatus.ACTIVE)),
                orZero(transactionRepository.sumAmountByTypes(List.of(TransactionType.DEPOSIT))),
                orZero(transactionRepository.sumAmountByTypes(List.of(TransactionType.WITHDRAWAL))),
                orZero(transactionRepository.sumAmountByTypes(List.of(TransactionType.LOAN_DISBURSEMENT))),
                loanApplicationRepository.getLoanApplicationNumbersByStatus()
        );
    }



    private BigDecimal orZero(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }
}
