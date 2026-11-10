package com.maverickbank.service;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.dto.response.AccountRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.AccountType;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.OwnershipException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.AccountMapper;
import com.maverickbank.model.Account;
import com.maverickbank.model.Customer;
import com.maverickbank.model.Employee;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class AccountService {
    private final CustomerRepository customerRepository;
    private final BranchService branchService;
    private final AccountRepository accountRepository;
    private final EmployeeService employeeService;

    public void createAccount(String customerUsername, AccountDto dto) {
        Customer customer = customerRepository.findByUsername(customerUsername);
        if(customer == null) throw new ResourceNotFoundException("Customer not found!");

        // Log: a new account is requested by the customer
        log.info("Customer {} requested a new {} account", customerUsername, dto.accountType());

        createBankAccount(customer, dto);
    }

    public Account createBankAccount(Customer customer, AccountDto dto) {
        Account account = AccountMapper.convertDtoToEntity(dto);
        account.setCustomer(customer);
        account.setBranch(branchService.getBranchById(dto.branchId()));
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountStatus(AccountStatus.PENDING);

        // Log: the account is saved with the status PENDING (the employee approves it later)
        log.info("Creating account {} with status PENDING", account.getAccountNumber());

        return accountRepository.save(account);
    }

    // Filter - the accounts of the customer can be filtered with the account type and the account status (both are optional)
    public List<AccountRespDto> getMyAccounts(String name, AccountType accountType, AccountStatus accountStatus) {
        // Log: accounts of the customer are listed
        log.info("Fetching accounts of customer {} (type: {}, status: {})", name, accountType, accountStatus);

        return accountRepository.getMyAccountsDynamically(name, accountType, accountStatus).stream().map(AccountMapper::convertEntityToDto).toList();
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
        // Log: customer opens the details of an own account
        log.info("Fetching account id {} of customer {}", id, name);

        Account account = getOwnedAccount(id, name);
        return AccountMapper.convertEntityToDto(account);
    }

    public AccountRespDto getAccountDetails(Long id) {
        // Log: employee opens the details of an account
        log.info("Fetching details of account id {}", id);

        return AccountMapper.convertEntityToDto(getById(id));
    }

    public AccountRespDto getAccountDetailsByNumber(String accountNumber) {
        // Log: employee searches an account with the account number
        log.info("Fetching details of account number {}", accountNumber);

        return AccountMapper.convertEntityToDto(getByAccountNumber(accountNumber));
    }

    // Filter - the accounts waiting for approval can be filtered with the account type and the branch (both are optional)
    public List<AccountRespDto> getPendingAccounts(AccountType accountType, Long branchId) {
        // Log: accounts waiting for the approval are listed
        log.info("Fetching the pending accounts (type: {}, branch id: {})", accountType, branchId);

        return accountRepository.getPendingAccountsDynamically(AccountStatus.PENDING, accountType, branchId).stream().map(AccountMapper :: convertEntityToDto).toList();
    }

    public void approveAccount(Long id, String name) {
        Account account = getById(id);

        if(account.getAccountStatus() != AccountStatus.PENDING)
            throw new InvalidCallException("Account is not waiting for approval!");

        Employee employee = employeeService.findByUsername(name);

        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setApprovedBy(employee);
        accountRepository.save(account);

        // Log: the employee has approved the account
        log.info("Account id {} approved by employee {}", id, name);
    }

    public String generateAccountNumber(){
        long min = 100_000_000_000L; // Smallest 12-digit number
        long max = 999_999_999_999L; // Largest 12-digit number

        String accountNumber;
        do {
            // max + 1 makes the upper limit inclusive
            accountNumber = String.valueOf(ThreadLocalRandom.current().nextLong(min, max + 1));
        } while (accountRepository.countAccountNumber(accountNumber) > 0);

        return accountNumber;
    }

    public Account getByAccountNumber(String accountNumber) {
        Account account = accountRepository.getByAccountNumber(accountNumber);
        if(account == null) throw new ResourceNotFoundException("Account number " + accountNumber + " not found!");

        return account;
    }

    public void closeAccount(Account account) {
        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

        // Log: the account is closed
        log.info("Account {} is closed", account.getAccountNumber());
    }

    public void credit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Log: money is added to the account
        log.info("Credited {} to account {}", amount, account.getAccountNumber());
    }

    public void debit(Account account, BigDecimal amount) {
        if(account.getBalance().compareTo(amount) < 0) throw new InvalidCallException("Insufficient Balance!");

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Log: money is taken from the account
        log.info("Debited {} from account {}", amount, account.getAccountNumber());
    }
}
