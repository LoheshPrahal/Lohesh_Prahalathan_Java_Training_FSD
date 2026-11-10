package com.maverickbank.mapper;

import com.maverickbank.dto.request.LoanDto;
import com.maverickbank.model.Loan;

public class LoanMapper {
    public static Loan convertDtoToEntity(LoanDto dto){
        Loan loan = new Loan();
        loan.setLoanType(dto.loanType());
        loan.setMinAmount(dto.minAmount());
        loan.setMaxAmount(dto.maxAmount());
        loan.setInterestRate(dto.interestRate());
        loan.setTenureMonths(dto.tenureMonths());
        return loan;
    }
}
