package com.maverickbank.mapper;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.model.Account;

public class AccountMapper {
    public static Account convertDtoToEntity(AccountDto dto){
        Account account = new Account();
        account.setAccountType(dto.accountType());

        return account;
    }
}
