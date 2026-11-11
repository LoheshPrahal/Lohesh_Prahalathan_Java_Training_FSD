package com.maverickbank.service;

import com.maverickbank.dto.request.BranchDto;
import com.maverickbank.enums.City;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Branch;
import com.maverickbank.repository.BranchRepository;
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
public class BranchServiceTest {
    @InjectMocks
    private BranchService branchService;
    @Mock
    private BranchRepository branchRepository;

    @Test
    public void addBranchTest() {
        BranchDto dto = new BranchDto("Main Branch", "Chennai", City.CHENNAI, "MAVB0000001");

        branchService.addBranch(dto);

        verify(branchRepository, times(1)).save(any(Branch.class));
    }

    @Test
    public void getBranchByIdTest() {
        Branch branch = new Branch();
        branch.setId(1L);
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        assertEquals(1L, branchService.getBranchById(1L).getId());
        verify(branchRepository, times(1)).findById(1L);
    }

    @Test
    public void getBranchByIdTest_Invalid() {
        when(branchRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> branchService.getBranchById(5L));
        verify(branchRepository, times(1)).findById(5L);
    }

    @Test
    public void getAllBranchesTest() {
        Branch branch = new Branch();
        when(branchRepository.findAll()).thenReturn(List.of(branch));

        assertEquals(1, branchService.getAllBranches().size());
        verify(branchRepository, times(1)).findAll();
    }
}
