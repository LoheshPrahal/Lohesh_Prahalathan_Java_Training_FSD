package com.maverickbank.service;

import com.maverickbank.dto.request.BeneficiaryDto;
import com.maverickbank.dto.response.BeneficiaryRespDto;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.mapper.BeneficiaryMapper;
import com.maverickbank.model.Account;
import com.maverickbank.model.Beneficiary;
import com.maverickbank.model.Branch;
import com.maverickbank.model.Customer;
import com.maverickbank.repository.BeneficiaryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {
    private final CustomerService customerService;
    private final BranchService branchService;
    private final AccountService accountService;
    private final BeneficiaryRepository beneficiaryRepository;

    public void add(String name, @Valid BeneficiaryDto dto) {
        Customer customer = customerService.findCustomerByUsername(name);
        Branch branch = branchService.getBranchById(dto.branchId());

        Account destinationAccount = accountService.getByAccountNumber(dto.accountNumber());

        if(destinationAccount.getBranch().getId() != dto.branchId()){
            throw new InvalidCallException("Beneficiary account does not belong to the selected branch!");
        }

        if(beneficiaryRepository.countSameBeneficiaryForCurrentCustomer(customer.getId(), dto.accountNumber())){
            throw new InvalidCallException("Beneficiary already present");
        }

        Beneficiary beneficiary = BeneficiaryMapper.convertDtoToEntity(dto);
        beneficiary.setCustomer(customer);
        beneficiary.setBranch(branch);
        beneficiaryRepository.save(beneficiary);
    }

    public List<BeneficiaryRespDto> getMyBeneficiaries(String name) {
        return beneficiaryRepository.getByCustomerUsername(name).stream().map(BeneficiaryMapper :: convertEntityToDto).toList();
    }
}
