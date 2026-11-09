package com.maverickbank.service;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.dto.response.AccountRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.OwnershipException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.AccountMapper;
import com.maverickbank.model.Account;
import com.maverickbank.model.Customer;
import com.maverickbank.model.Employee;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.CustomerRepository;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final CustomerRepository customerRepository;
    private final BranchService branchService;
    private final AccountRepository accountRepository;
    private final EmployeeService employeeService;


    public void createAccount(String customerUsername, AccountDto dto) {
        Customer customer = customerRepository.findByUsername(customerUsername);

        if(customer == null) throw new ResourceNotFoundException("Customer not found!");

        createBankAccount(customer, dto);
    }

    public Account createBankAccount(Customer customer, AccountDto dto) {
        Account account = AccountMapper.convertDtoToEntity(dto);
        account.setCustomer(customer);
        account.setBranch(branchService.getBranchById(dto.branchId()));
        account.setBalance(BigDecimal.ZERO);
        account.setAccountStatus(AccountStatus.PENDING);

        return accountRepository.save(account);
    }

    public List<AccountRespDto> getMyAccounts(String name) {
        return accountRepository.getAccountsByCustomerUsername(name).stream().map(AccountMapper::convertEntityToDto).toList();
    }

    public Account getById(Long id){
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
    }

    public Account getOwnedAccount(Long id, String name){
        Account account = getById(id);
        if(!account.getCustomer().getUser().getUsername().equals(name)){
            throw new OwnershipException("This account does not belong to the currently logged in customer");
        }
        return account;
    }
    public AccountRespDto getMyAccountById(Long id, String name) {
        Account account = getOwnedAccount(id, name);

        return AccountMapper.convertEntityToDto(account);

    }

    public List<AccountRespDto> getPendingAccounts() {
        return accountRepository.getPendingAccounts(AccountStatus.PENDING).stream().map(AccountMapper :: convertEntityToDto).toList();
    }

    public void approveAccount(Long id, String name) {
        Account account = getById(id);

        if(account.getAccountStatus() != AccountStatus.PENDING) throw new InvalidCallException("Account is not waiting for approval!");
        Employee employee = employeeService.findByUsername(name);

        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setApprovedBy(employee);
        accountRepository.save(account);
    }

    public String generateAccountNumber(){

        long min = 100_000_000_000L; // Smallest 12-digit number
        long max = 999_999_999_999L; // Largest 12-digit number

        // max + 1 makes the upper limit inclusive
        long accountNumber = ThreadLocalRandom.current().nextLong(min, max + 1);
        while(accountRepository.countAccountNumber(accountNumber) > 0){
            accountNumber = ThreadLocalRandom.current().nextLong(min, max + 1);
        }
        return String.valueOf(accountNumber);
    }

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.getByAccountNumber(accountNumber);
    }

    public void closeAccount(Account account) {
        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
    }

    public void credit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public void debit(Account account, BigDecimal amount) {
        if(account.getBalance().compareTo(amount) < 0) throw new InvalidCallException("Insufficient Balance!");

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }
}
