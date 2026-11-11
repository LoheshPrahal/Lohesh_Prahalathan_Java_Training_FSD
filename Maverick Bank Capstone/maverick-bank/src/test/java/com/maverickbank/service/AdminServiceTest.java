package com.maverickbank.service;

import com.maverickbank.dto.request.AdminDto;
import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Admin;
import com.maverickbank.model.User;
import com.maverickbank.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @InjectMocks
    private AdminService adminService;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserService userService;

    @Test
    public void insertAdminTest() {
        AdminDto dto = new AdminDto("John", "john@gmail.com", "john", "password123");
        User user = new User();
        when(userService.getUserObject("john", "password123", UserRole.ADMIN)).thenReturn(user);

        adminService.insertAdmin(dto);

        verify(userService, times(1)).getUserObject("john", "password123", UserRole.ADMIN);
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    public void getAdminByIdTest() {
        Admin admin = new Admin();
        admin.setId(1L);
        User user = new User();
        user.setUsername("john");
        admin.setUser(user);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertEquals(1L, adminService.getAdminById(1L).adminId());
        verify(adminRepository, times(1)).findById(1L);
    }

    @Test
    public void getAdminByIdTest_Invalid() {
        when(adminRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminById(5L));
        verify(adminRepository, times(1)).findById(5L);
    }

    @Test
    public void getAllAdminTest() {
        Admin admin = new Admin();
        User user = new User();
        user.setUsername("john");
        admin.setUser(user);
        when(adminRepository.findAll()).thenReturn(List.of(admin));

        assertEquals(1, adminService.getAllAdmin().size());
        verify(adminRepository, times(1)).findAll();
    }
}
