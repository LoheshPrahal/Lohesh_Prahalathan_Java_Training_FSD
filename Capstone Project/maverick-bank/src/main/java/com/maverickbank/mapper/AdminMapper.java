package com.maverickbank.mapper;

import com.maverickbank.dto.request.AdminDto;
import com.maverickbank.model.Admin;

public class AdminMapper {
    public static Admin convertDtoToEntity(AdminDto dto){
        Admin admin = new Admin();
        admin.setName(dto.name());
        admin.setEmail(dto.email());
        return admin;
    }
}
