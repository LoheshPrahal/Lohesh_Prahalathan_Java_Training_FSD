package com.maverickbank.controller;

import com.maverickbank.dto.request.BranchDto;
import com.maverickbank.model.Branch;
import com.maverickbank.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @PostMapping("/api/branch/add")
    public void addBranch(@Valid @RequestBody BranchDto dto){
        branchService.addBranch(dto);
    }

    @GetMapping("/api/branch/all")
    public List<Branch> getAllBranches(){
        return branchService.getAllBranches();
    }
}
