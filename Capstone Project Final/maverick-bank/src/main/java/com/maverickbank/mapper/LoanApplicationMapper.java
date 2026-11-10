package com.maverickbank.mapper;

import com.maverickbank.dto.request.LoanApplicationDto;
import com.maverickbank.dto.response.LoanApplicationRespDto;
import com.maverickbank.model.LoanApplication;

public class LoanApplicationMapper {
    public static LoanApplication convertDtoToEntity(LoanApplicationDto dto){
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setRequestedAmount(dto.requestedAmount());
        loanApplication.setPurpose(dto.purpose());
        return loanApplication;
    }

    // Repayment - used when one loan is fetched with the id
    public static LoanApplicationRespDto convertEntityToDto(LoanApplication loanApplication){
        return new LoanApplicationRespDto(
                loanApplication.getId(),
                loanApplication.getLoan().getLoanType(),
                loanApplication.getAccount().getId(),
                loanApplication.getAccount().getAccountNumber(),
                loanApplication.getAccount().getCustomer().getName(),
                loanApplication.getRequestedAmount(),
                loanApplication.getLoan().getInterestRate(),
                loanApplication.getLoan().getTenureMonths(),
                loanApplication.getPurpose(),
                loanApplication.getApplicationDate(),
                loanApplication.getStatus(),
                loanApplication.getReviewedDate(),
                loanApplication.getRemainingAmount()
        );
    }
}
