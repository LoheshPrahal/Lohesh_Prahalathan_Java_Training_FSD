package com.maverickbank.mapper;

import com.maverickbank.dto.request.BeneficiaryDto;
import com.maverickbank.dto.response.BeneficiaryRespDto;
import com.maverickbank.model.Beneficiary;

public class BeneficiaryMapper {
    public static Beneficiary convertDtoToEntity(BeneficiaryDto dto){
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setAccountName(dto.accountName());
        beneficiary.setAccountNumber(dto.accountNumber());
        return beneficiary;
    }

    public static BeneficiaryRespDto convertEntityToDto(Beneficiary beneficiary){
        return new BeneficiaryRespDto(
                beneficiary.getId(),
                beneficiary.getAccountName(),
                beneficiary.getAccountNumber(),
                beneficiary.getBranch().getName(),
                beneficiary.getBranch().getIfscCode()
        );
    }
}
