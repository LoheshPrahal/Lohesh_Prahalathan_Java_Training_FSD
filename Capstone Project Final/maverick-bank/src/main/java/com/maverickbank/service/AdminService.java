package com.maverickbank.service;

import com.maverickbank.dto.request.AdminDto;
import com.maverickbank.dto.response.AdminRespDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.AdminMapper;
import com.maverickbank.model.Admin;
import com.maverickbank.model.User;
import com.maverickbank.repository.AdminRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class AdminService {
    private final UserService userService;
    private final AdminRepository adminRepository;

    public void insertAdmin(@Valid AdminDto dto) {
        Admin admin = AdminMapper.convertDtoToEntity(dto);
        User user = userService.getUserObject(dto.username(), dto.password(), UserRole.ADMIN);
        admin.setUser(user);
        adminRepository.save(admin);

        // Log: a new admin is added
        log.info("Admin {} is added", dto.username());
    }

    public AdminRespDto getAdminById(Long id) {
        // Log: details of an admin are fetched
        log.info("Fetching admin id {}", id);

        Admin admin = adminRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Admin with id " + id + " does not exist!"));
        return AdminMapper.convertEntityToDto(admin);
    }

    public List<AdminRespDto> getAllAdmin() {
        // Log: all the admins are listed
        log.info("Fetching all the admins");

        return adminRepository.findAll().stream().map(AdminMapper :: convertEntityToDto).toList();
    }
}
