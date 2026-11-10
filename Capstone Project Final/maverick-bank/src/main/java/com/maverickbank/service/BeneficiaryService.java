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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
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

        if(beneficiaryRepository.countSameBeneficiaryForCurrentCustomer(customer.getId(), dto.accountNumber()) > 0){
            throw new InvalidCallException("Beneficiary already present");
        }

        Beneficiary beneficiary = BeneficiaryMapper.convertDtoToEntity(dto);
        beneficiary.setCustomer(customer);
        beneficiary.setBranch(branch);
        beneficiaryRepository.save(beneficiary);

        // Log: the customer has added a beneficiary
        log.info("Beneficiary account {} added by customer {}", dto.accountNumber(), name);
    }

    public List<BeneficiaryRespDto> getMyBeneficiaries(String name) {
        // Log: beneficiaries of the customer are listed
        log.info("Fetching beneficiaries of customer {}", name);

        return beneficiaryRepository.getByCustomerUsername(name).stream().map(BeneficiaryMapper :: convertEntityToDto).toList();
    }
}
