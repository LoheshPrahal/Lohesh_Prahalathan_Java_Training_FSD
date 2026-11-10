package com.maverickbank.mapper;

import com.maverickbank.dto.request.BranchDto;
import com.maverickbank.model.Branch;

public class BranchMapper {
       public static Branch convertDtoToEntity(BranchDto dto){
        Branch branch = new Branch();
        branch.setName(dto.name());
        branch.setAddress(dto.address());
        branch.setCity(dto.city());
        branch.setIfscCode(dto.ifscCode());
        return branch;
    }
}
