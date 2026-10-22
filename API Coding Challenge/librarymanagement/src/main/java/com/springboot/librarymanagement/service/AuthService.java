package com.springboot.librarymanagement.service;

import com.springboot.librarymanagement.dto.request.RegisterDto;
import com.springboot.librarymanagement.model.User;
import com.springboot.librarymanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto dto) {
        User user = userService.getUserObject(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }
}
