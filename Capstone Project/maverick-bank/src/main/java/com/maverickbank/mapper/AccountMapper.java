package com.maverickbank.mapper;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.dto.response.AccountRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.model.Account;

public class AccountMapper {
    public static Account convertDtoToEntity(AccountDto dto){
        Account account = new Account();
        account.setAccountType(dto.accountType());

        return account;
    }

    public static AccountRespDto convertEntityToDto(Account account){
        return new AccountRespDto(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getAccountStatus(),
                account.getOpenedDate(),
                account.getCustomer().getName(),
                account.getBranch().getName(),
                account.getBranch().getAddress(),
                account.getBranch().getIfscCode()
        );

    }
}
