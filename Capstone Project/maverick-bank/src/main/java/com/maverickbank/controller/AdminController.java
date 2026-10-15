package com.maverickbank.controller;

import com.maverickbank.dto.request.AdminDto;
import com.maverickbank.model.Admin;
import com.maverickbank.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/api/admin/add")
    public void insertAdmin(@Valid @RequestBody AdminDto dto){
        adminService.insertAdmin(dto);
    }

    @GetMapping("/api/admin/{id}")
    public Admin getAdminById(@PathVariable long id){
        return adminService.getAdminById(id);
    }

    @GetMapping("/api/admin/all")
    public List<Admin> getAllAdmin(){
        return adminService.getAllAdmin();
    }
}
