package com.maverickbank.service;

import com.maverickbank.dto.request.AccountDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.AccountType;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.OwnershipException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Account;
import com.maverickbank.model.Branch;
import com.maverickbank.model.Customer;
import com.maverickbank.model.Employee;
import com.maverickbank.model.User;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BranchService branchService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmployeeService employeeService;

    @Test
    public void createAccountTest() {
        Customer customer = new Customer();
        Branch branch = new Branch();
        AccountDto dto = new AccountDto(AccountType.SAVINGS, 1L);
        when(customerRepository.findByUsername("john")).thenReturn(customer);
        when(branchService.getBranchById(1L)).thenReturn(branch);

        accountService.createAccount("john", dto);

        verify(customerRepository, times(1)).findByUsername("john");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void createAccountTest_Invalid() {
        when(customerRepository.findByUsername("missing")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.createAccount("missing", new AccountDto(AccountType.SAVINGS, 1L)));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void createBankAccountTest() {
        Customer customer = new Customer();
        Branch branch = new Branch();
        when(branchService.getBranchById(1L)).thenReturn(branch);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account account = accountService.createBankAccount(customer, new AccountDto(AccountType.SAVINGS, 1L));

        assertEquals(AccountStatus.PENDING, account.getAccountStatus());
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertEquals(customer, account.getCustomer());
        assertEquals(12, account.getAccountNumber().length());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void getMyAccountsTest() {
        Account account = new Account();
        account.setCustomer(new Customer());
        account.setBranch(new Branch());
        when(accountRepository.getMyAccountsDynamically("john", AccountType.SAVINGS, AccountStatus.ACTIVE))
                .thenReturn(List.of(account));

        assertEquals(1, accountService.getMyAccounts("john", AccountType.SAVINGS, AccountStatus.ACTIVE).size());
        verify(accountRepository, times(1)).getMyAccountsDynamically("john", AccountType.SAVINGS, AccountStatus.ACTIVE);
    }

    @Test
    public void getByIdTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertEquals(1L, accountService.getById(1L).getId());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void getByIdTest_Invalid() {
        when(accountRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getById(5L));
        verify(accountRepository, times(1)).findById(5L);
    }

    @Test
    public void getOwnedAccountTest() {
        Account account = new Account();
        Customer customer = new Customer();
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        account.setCustomer(customer);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertEquals(account, accountService.getOwnedAccount(1L, "john"));
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void getOwnedAccountTest_Invalid() {
        Account account = new Account();
        Customer customer = new Customer();
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        account.setCustomer(customer);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(OwnershipException.class, () -> accountService.getOwnedAccount(1L, "other"));
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void getMyAccountByIdTest() {
        Account account = new Account();
        Customer customer = new Customer();
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        account.setCustomer(customer);
        account.setBranch(new Branch());
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertEquals(1L, accountService.getMyAccountById(1L, "john").accountId());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void getAccountDetailsTest() {
        Account account = new Account();
        account.setId(1L);
        account.setCustomer(new Customer());
        account.setBranch(new Branch());
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertEquals(1L, accountService.getAccountDetails(1L).accountId());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void getAccountDetailsByNumberTest() {
        Account account = new Account();
        account.setAccountNumber("123456789012");
        account.setCustomer(new Customer());
        account.setBranch(new Branch());
        when(accountRepository.getByAccountNumber("123456789012")).thenReturn(account);

        assertEquals("123456789012", accountService.getAccountDetailsByNumber("123456789012").accountNumber());
        verify(accountRepository, times(1)).getByAccountNumber("123456789012");
    }

    @Test
    public void getPendingAccountsTest() {
        Account account = new Account();
        account.setCustomer(new Customer());
        account.setBranch(new Branch());
        when(accountRepository.getPendingAccountsDynamically(AccountStatus.PENDING, AccountType.SAVINGS, 1L))
                .thenReturn(List.of(account));

        assertEquals(1, accountService.getPendingAccounts(AccountType.SAVINGS, 1L).size());
        verify(accountRepository, times(1)).getPendingAccountsDynamically(AccountStatus.PENDING, AccountType.SAVINGS, 1L);
    }

    @Test
    public void approveAccountTest() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.PENDING);
        Employee employee = new Employee();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(employeeService.findByUsername("john")).thenReturn(employee);

        accountService.approveAccount(1L, "john");

        assertEquals(AccountStatus.ACTIVE, account.getAccountStatus());
        assertEquals(employee, account.getApprovedBy());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void approveAccountTest_Invalid() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InvalidCallException.class, () -> accountService.approveAccount(1L, "john"));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void generateAccountNumberTest() {
        String accountNumber = accountService.generateAccountNumber();

        assertEquals(12, accountNumber.length());
        verify(accountRepository, times(1)).countAccountNumber(accountNumber);
    }

    @Test
    public void getByAccountNumberTest() {
        Account account = new Account();
        when(accountRepository.getByAccountNumber("123456789012")).thenReturn(account);

        assertEquals(account, accountService.getByAccountNumber("123456789012"));
        verify(accountRepository, times(1)).getByAccountNumber("123456789012");
    }

    @Test
    public void getByAccountNumberTest_Invalid() {
        when(accountRepository.getByAccountNumber("missing")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> accountService.getByAccountNumber("missing"));
        verify(accountRepository, times(1)).getByAccountNumber("missing");
    }

    @Test
    public void closeAccountTest() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);

        accountService.closeAccount(account);

        assertEquals(AccountStatus.CLOSED, account.getAccountStatus());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void creditTest() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));

        accountService.credit(account, new BigDecimal("50"));

        assertEquals(new BigDecimal("150"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void debitTest() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));

        accountService.debit(account, new BigDecimal("50"));

        assertEquals(new BigDecimal("50"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void debitTest_InsufficientBalance() {
        Account account = new Account();
        account.setBalance(new BigDecimal("10"));

        assertThrows(InvalidCallException.class, () -> accountService.debit(account, new BigDecimal("50")));
        verify(accountRepository, times(0)).save(any(Account.class));
    }
}
