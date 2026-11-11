package com.maverickbank.service;

import com.maverickbank.dto.response.FinancialReportDto;
import com.maverickbank.dto.response.LoanStatusCountDto;
import com.maverickbank.dto.response.TransactionRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.LoanStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.LoanApplicationRepository;
import com.maverickbank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Test
    public void getFinancialReportTest() {
        when(accountRepository.countByAccountStatus(AccountStatus.ACTIVE)).thenReturn(2L);
        when(accountRepository.sumBalanceByAccountStatus(AccountStatus.ACTIVE)).thenReturn(new BigDecimal("5000"));
        when(transactionRepository.sumAmountByTypes(List.of(TransactionType.DEPOSIT))).thenReturn(new BigDecimal("3000"));
        when(transactionRepository.sumAmountByTypes(List.of(TransactionType.WITHDRAWAL))).thenReturn(new BigDecimal("1000"));
        when(transactionRepository.sumAmountByTypes(List.of(TransactionType.LOAN_DISBURSEMENT))).thenReturn(new BigDecimal("2000"));
        when(loanApplicationRepository.getLoanApplicationNumbersByStatus())
                .thenReturn(List.of(new LoanStatusCountDto(LoanStatus.PENDING, 1L)));

        FinancialReportDto report = reportService.getFinancialReport();

        assertEquals(2L, report.activeAccounts());
        assertEquals(new BigDecimal("5000"), report.totalBalance());
        assertEquals(new BigDecimal("3000"), report.totalDeposits());
        assertEquals(1, report.loanApplicationsByStatus().size());
        verify(accountRepository, times(1)).countByAccountStatus(AccountStatus.ACTIVE);
        verify(loanApplicationRepository, times(1)).getLoanApplicationNumbersByStatus();
    }

    @Test
    public void getFinancialReportTest_NoTransactions() {
        when(accountRepository.countByAccountStatus(AccountStatus.ACTIVE)).thenReturn(0L);

        FinancialReportDto report = reportService.getFinancialReport();

        assertEquals(BigDecimal.ZERO, report.totalBalance());
        assertEquals(BigDecimal.ZERO, report.totalDeposits());
        assertEquals(BigDecimal.ZERO, report.totalWithdrawals());
        assertEquals(BigDecimal.ZERO, report.totalLoansDisbursed());
    }

    @Test
    public void getRegulatoryReportTest() {
        BigDecimal threshold = new BigDecimal("10000");
        TransactionRespDto dto = new TransactionRespDto(1L, "123", TransactionType.DEPOSIT,
                threshold, null, "Deposit", null);
        when(transactionRepository.getHighValueTransactions(threshold, PageRequest.of(0, 5)))
                .thenReturn(List.of(dto));

        assertEquals(1, reportService.getRegulatoryReport(threshold, 0, 5).size());
        verify(transactionRepository, times(1)).getHighValueTransactions(threshold, PageRequest.of(0, 5));
    }
}
