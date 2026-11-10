package com.maverickbank.service;

import com.maverickbank.enums.AccountStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.OwnershipException;
import com.maverickbank.model.Account;
import com.maverickbank.model.Customer;
import com.maverickbank.model.Employee;
import com.maverickbank.model.User;
import com.maverickbank.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmployeeService employeeService;

    private Account getAccount(String username, String balance, AccountStatus status){
        User user = new User();
        user.setUsername(username);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUser(user);

        Account account = new Account();
        account.setId(1L);
        account.setCustomer(customer);
        account.setBalance(new BigDecimal(balance));
        account.setAccountStatus(status);
        return account;
    }

    @Test
    public void creditTest(){
        Account account = getAccount("john", "100.00", AccountStatus.ACTIVE);

        accountService.credit(account, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void debitTest(){
        Account account = getAccount("john", "100.00", AccountStatus.ACTIVE);

        accountService.debit(account, new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void debitTest_InsufficientBalance(){
        Account account = getAccount("john", "100.00", AccountStatus.ACTIVE);

        assertThrows(InvalidCallException.class,
                () -> accountService.debit(account, new BigDecimal("500.00")));

        assertEquals(new BigDecimal("100.00"), account.getBalance());
        verifyNoInteractions(accountRepository);
    }

    @Test
    public void getOwnedAccountTest(){
        Account account = getAccount("john", "100.00", AccountStatus.ACTIVE);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertEquals(account, accountService.getOwnedAccount(1L, "john"));

        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void getOwnedAccountTest_AnotherCustomer(){
        Account account = getAccount("john", "100.00", AccountStatus.ACTIVE);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(OwnershipException.class,
                () -> accountService.getOwnedAccount(1L, "peter"));
    }

    @Test
    public void approveAccountTest(){
        Account account = getAccount("john", "0.00", AccountStatus.PENDING);
        Employee employee = new Employee();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(employeeService.findByUsername("emp1")).thenReturn(employee);

        accountService.approveAccount(1L, "emp1");

        assertEquals(AccountStatus.ACTIVE, account.getAccountStatus());
        assertEquals(employee, account.getApprovedBy());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void approveAccountTest_NotPending(){
        Account account = getAccount("john", "0.00", AccountStatus.ACTIVE);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InvalidCallException.class,
                () -> accountService.approveAccount(1L, "emp1"));

        verify(accountRepository, never()).save(any(Account.class));
    }
}
