package com.maverickbank.service;

import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Branch;
import com.maverickbank.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public Branch getBranchById(long branchId){
        return branchRepository.findById(branchId).orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
    }
}
