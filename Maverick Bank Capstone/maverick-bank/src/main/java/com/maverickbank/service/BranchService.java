package com.maverickbank.service;

import com.maverickbank.dto.request.BranchDto;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.BranchMapper;
import com.maverickbank.model.Branch;
import com.maverickbank.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchService {
    private final BranchRepository branchRepository;

    public Branch getBranchById(Long branchId){
        return branchRepository.findById(branchId).orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
    }

    public void addBranch(BranchDto dto) {
        Branch branch = BranchMapper.convertDtoToEntity(dto);
        branchRepository.save(branch);

        log.info("Branch {} ({}) is added", dto.name(), dto.ifscCode());
    }

    public List<Branch> getAllBranches() {
        log.info("Fetching all the branches");

        return branchRepository.findAll();
    }


}
