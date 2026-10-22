package com.springboot.librarymanagement.mapper;

import com.springboot.librarymanagement.dto.request.RegisterDto;
import com.springboot.librarymanagement.model.User;

public class UserMapper {
    public static User convertDtoToEntity(RegisterDto dto){
        User user = new User();
        user.setUsername(dto.username());
        user.setRole(dto.role());
        return user;
    }
}
