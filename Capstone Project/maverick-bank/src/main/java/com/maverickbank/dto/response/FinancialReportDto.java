package com.maverickbank.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record FinancialReportDto(
        long activeAccounts,
        BigDecimal totalBalance,
        BigDecimal totalDeposits,
        BigDecimal totalWithdrawals,
        BigDecimal totalLoansDisbursed,
        List<LoanStatusCountDto> loanApplicationsByStatus
) {
}
