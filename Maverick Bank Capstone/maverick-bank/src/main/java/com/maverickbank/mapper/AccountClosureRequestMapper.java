package com.maverickbank.mapper;

import com.maverickbank.dto.request.AccountClosureDto;
import com.maverickbank.model.AccountClosureRequest;

public class AccountClosureRequestMapper {
    public static AccountClosureRequest convertDtoToEntity(AccountClosureDto dto){
        AccountClosureRequest request = new AccountClosureRequest();
        request.setReason(dto.reason());
        return request;
    }
}
