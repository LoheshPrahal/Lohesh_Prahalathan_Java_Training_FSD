package com.maverickbank.dto.response;

import java.util.List;

public record FinancialReportDto(
        Long activeAccounts,
        Double totalBalance,
        Double totalDeposits,
        Double totalWithdrawals,
        Double totalLoansDisbursed,
        List<LoanStatusCountDto> loanApplicationsByStatus
) {
}
