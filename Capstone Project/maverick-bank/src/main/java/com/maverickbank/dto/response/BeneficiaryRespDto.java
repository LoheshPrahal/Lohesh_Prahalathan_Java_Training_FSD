package com.maverickbank.dto.response;

public record BeneficiaryRespDto(
        long beneficiaryId,
        String accountName,
        String accountNumber,
        String branchName,
        String ifscCode
) {
}
