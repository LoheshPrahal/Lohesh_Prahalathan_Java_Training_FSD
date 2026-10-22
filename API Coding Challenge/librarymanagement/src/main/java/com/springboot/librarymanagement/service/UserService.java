package com.springboot.librarymanagement.service;

import com.springboot.librarymanagement.dto.request.RegisterDto;
import com.springboot.librarymanagement.mapper.UserMapper;
import com.springboot.librarymanagement.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public User getUserObject(RegisterDto dto) {
        return UserMapper.convertDtoToEntity(dto);
    }
}
