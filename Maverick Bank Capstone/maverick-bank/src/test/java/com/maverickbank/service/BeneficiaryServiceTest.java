package com.maverickbank.service;

import com.maverickbank.dto.request.BeneficiaryDto;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.model.Account;
import com.maverickbank.model.Beneficiary;
import com.maverickbank.model.Branch;
import com.maverickbank.model.Customer;
import com.maverickbank.repository.BeneficiaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeneficiaryServiceTest {
    @InjectMocks
    private BeneficiaryService beneficiaryService;
    @Mock
    private CustomerService customerService;
    @Mock
    private BranchService branchService;
    @Mock
    private AccountService accountService;
    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Test
    public void addTest() {
        BeneficiaryDto dto = new BeneficiaryDto("John", "123456789012", 1L);
        Customer customer = new Customer();
        customer.setId(1L);
        Branch branch = new Branch();
        branch.setId(1L);
        Account account = new Account();
        account.setBranch(branch);
        when(customerService.findCustomerByUsername("john")).thenReturn(customer);
        when(branchService.getBranchById(1L)).thenReturn(branch);
        when(accountService.getByAccountNumber(dto.accountNumber())).thenReturn(account);

        beneficiaryService.add("john", dto);

        verify(beneficiaryRepository, times(1)).countSameBeneficiaryForCurrentCustomer(1L, "123456789012");
        verify(beneficiaryRepository, times(1)).save(any(Beneficiary.class));
    }

    @Test
    public void addTest_InvalidBranch() {
        BeneficiaryDto dto = new BeneficiaryDto("John", "123456789012", 1L);
        Customer customer = new Customer();
        Branch branch = new Branch();
        branch.setId(2L);
        Account account = new Account();
        account.setBranch(branch);
        when(customerService.findCustomerByUsername("john")).thenReturn(customer);
        when(branchService.getBranchById(1L)).thenReturn(new Branch());
        when(accountService.getByAccountNumber(dto.accountNumber())).thenReturn(account);

        assertThrows(InvalidCallException.class, () -> beneficiaryService.add("john", dto));
        verify(beneficiaryRepository, times(0)).save(any(Beneficiary.class));
    }

    @Test
    public void addTest_Duplicate() {
        BeneficiaryDto dto = new BeneficiaryDto("John", "123456789012", 1L);
        Customer customer = new Customer();
        customer.setId(1L);
        Branch branch = new Branch();
        branch.setId(1L);
        Account account = new Account();
        account.setBranch(branch);
        when(customerService.findCustomerByUsername("john")).thenReturn(customer);
        when(branchService.getBranchById(1L)).thenReturn(branch);
        when(accountService.getByAccountNumber(dto.accountNumber())).thenReturn(account);
        when(beneficiaryRepository.countSameBeneficiaryForCurrentCustomer(1L, "123456789012")).thenReturn(1L);

        assertThrows(InvalidCallException.class, () -> beneficiaryService.add("john", dto));
        verify(beneficiaryRepository, times(0)).save(any(Beneficiary.class));
    }

    @Test
    public void getMyBeneficiariesTest() {
        Branch branch = new Branch();
        branch.setName("Main Branch");
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBranch(branch);
        when(beneficiaryRepository.getByCustomerUsername("john")).thenReturn(List.of(beneficiary));

        assertEquals(1, beneficiaryService.getMyBeneficiaries("john").size());
        verify(beneficiaryRepository, times(1)).getByCustomerUsername("john");
    }
}
