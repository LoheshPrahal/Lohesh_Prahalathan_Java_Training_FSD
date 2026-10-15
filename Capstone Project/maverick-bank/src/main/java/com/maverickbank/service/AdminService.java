package com.maverickbank.service;

import com.maverickbank.dto.request.AdminDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.AdminMapper;
import com.maverickbank.model.Admin;
import com.maverickbank.model.User;
import com.maverickbank.repository.AdminRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final AdminRepository adminRepository;

    public void insertAdmin(@Valid AdminDto dto) {
        Admin admin = AdminMapper.convertDtoToEntity(dto);
        User user = userService.getUserObject(dto.username(), dto.password(), UserRole.ADMIN);
        admin.setUser(user);
        adminRepository.save(admin);
    }

    public Admin getAdminById(long id) {
        return adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin with id " + id + " does not exist!"));
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }
}
