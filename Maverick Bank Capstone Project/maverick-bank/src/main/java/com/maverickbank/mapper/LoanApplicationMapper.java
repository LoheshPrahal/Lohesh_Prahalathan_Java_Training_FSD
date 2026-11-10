package com.maverickbank.mapper;

import com.maverickbank.dto.request.LoanApplicationDto;
import com.maverickbank.model.LoanApplication;

public class LoanApplicationMapper {
    public static LoanApplication convertDtoToEntity(LoanApplicationDto dto){
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setRequestedAmount(dto.requestedAmount());
        loanApplication.setPurpose(dto.purpose());
        return loanApplication;
    }
}
