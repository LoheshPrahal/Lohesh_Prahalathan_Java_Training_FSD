package com.maverickbank.service;

import com.maverickbank.dto.request.TransactionDto;
import com.maverickbank.dto.request.TransferDto;
import com.maverickbank.dto.response.AccountSummaryDto;
import com.maverickbank.dto.response.TransactionRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.TransactionStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.OwnershipException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Account;
import com.maverickbank.model.Beneficiary;
import com.maverickbank.model.Customer;
import com.maverickbank.model.Transaction;
import com.maverickbank.model.User;
import com.maverickbank.repository.BeneficiaryRepository;
import com.maverickbank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Test
    public void depositTest() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        TransactionDto dto = new TransactionDto(1L, new BigDecimal("500"), "Salary");
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        transactionService.deposit("john", dto);

        verify(accountService, times(1)).credit(account, new BigDecimal("500"));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void depositTest_InactiveAccount() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.CLOSED);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        assertThrows(InvalidCallException.class,
                () -> transactionService.deposit("john", new TransactionDto(1L, new BigDecimal("500"), null)));
        verify(accountService, times(0)).credit(any(Account.class), any(BigDecimal.class));
    }

    @Test
    public void recordTransactionTest() {
        Account account = new Account();
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transactionService.recordTransaction(account, TransactionType.DEPOSIT,
                new BigDecimal("500"), "Salary", null);

        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
        assertEquals(TransactionStatus.SUCCESS, transaction.getTransactionStatus());
        assertEquals(new BigDecimal("500"), transaction.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Cash Deposit", transactionService.getDescription(null, "Cash Deposit"));
        assertEquals("Cash Deposit", transactionService.getDescription("", "Cash Deposit"));
        assertEquals("Salary", transactionService.getDescription("Salary", "Cash Deposit"));
    }

    @Test
    public void withdrawTest() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        TransactionDto dto = new TransactionDto(1L, new BigDecimal("200"), null);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        transactionService.withdraw("john", dto);

        verify(accountService, times(1)).debit(account, new BigDecimal("200"));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void transferTest() {
        Customer customer = new Customer();
        customer.setId(1L);
        Account source = new Account();
        source.setId(1L);
        source.setCustomer(customer);
        source.setAccountStatus(AccountStatus.ACTIVE);
        source.setAccountNumber("123456789012");
        Account destination = new Account();
        destination.setId(2L);
        destination.setAccountStatus(AccountStatus.ACTIVE);
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setCustomer(customer);
        beneficiary.setAccountNumber("987654321098");
        beneficiary.setAccountName("Jane");
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(2L)).thenReturn(Optional.of(beneficiary));
        when(accountService.getByAccountNumber("987654321098")).thenReturn(destination);

        transactionService.transfer("john", new TransferDto(1L, 2L, new BigDecimal("100"), null));

        verify(accountService, times(1)).debit(source, new BigDecimal("100"));
        verify(accountService, times(1)).credit(destination, new BigDecimal("100"));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    public void transferTest_BeneficiaryNotFound() {
        Account source = new Account();
        source.setAccountStatus(AccountStatus.ACTIVE);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.transfer("john", new TransferDto(1L, 2L, new BigDecimal("100"), null)));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    public void transferTest_InvalidBeneficiaryOwner() {
        Customer customer = new Customer();
        customer.setId(1L);
        Customer other = new Customer();
        other.setId(2L);
        Account source = new Account();
        source.setCustomer(customer);
        source.setAccountStatus(AccountStatus.ACTIVE);
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setCustomer(other);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(2L)).thenReturn(Optional.of(beneficiary));

        assertThrows(OwnershipException.class,
                () -> transactionService.transfer("john", new TransferDto(1L, 2L, new BigDecimal("100"), null)));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    public void transferTest_InactiveDestination() {
        Customer customer = new Customer();
        customer.setId(1L);
        Account source = new Account();
        source.setCustomer(customer);
        source.setAccountStatus(AccountStatus.ACTIVE);
        Account destination = new Account();
        destination.setAccountStatus(AccountStatus.CLOSED);
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setCustomer(customer);
        beneficiary.setAccountNumber("987654321098");
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(2L)).thenReturn(Optional.of(beneficiary));
        when(accountService.getByAccountNumber("987654321098")).thenReturn(destination);

        assertThrows(InvalidCallException.class,
                () -> transactionService.transfer("john", new TransferDto(1L, 2L, new BigDecimal("100"), null)));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    public void transferTest_SameAccount() {
        Customer customer = new Customer();
        customer.setId(1L);
        Account source = new Account();
        source.setId(1L);
        source.setCustomer(customer);
        source.setAccountStatus(AccountStatus.ACTIVE);
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setCustomer(customer);
        beneficiary.setAccountNumber("123456789012");
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(2L)).thenReturn(Optional.of(beneficiary));
        when(accountService.getByAccountNumber("123456789012")).thenReturn(source);

        assertThrows(InvalidCallException.class,
                () -> transactionService.transfer("john", new TransferDto(1L, 2L, new BigDecimal("100"), null)));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    public void getTransactionsByAccountDynamicFilterTest() {
        Account account = new Account();
        Customer customer = new Customer();
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        account.setCustomer(customer);
        when(accountService.getByAccountNumber("123456789012")).thenReturn(account);
        when(transactionRepository.getMyTransactionsDynamically("123456789012", null, null,
                TransactionType.DEPOSIT, PageRequest.of(0, 5))).thenReturn(List.of(
                        new TransactionRespDto(1L, "123456789012", TransactionType.DEPOSIT,
                                new BigDecimal("100"), Instant.now(), "Salary", TransactionStatus.SUCCESS)));

        assertEquals(1, transactionService.getTransactionsByAccountDynamicFilter(
                "john", "123456789012", null, null, TransactionType.DEPOSIT, 0, 5).size());
        verify(transactionRepository, times(1)).getMyTransactionsDynamically("123456789012", null, null,
                TransactionType.DEPOSIT, PageRequest.of(0, 5));
    }

    @Test
    public void getTransactionsByAccountDynamicFilterTest_InvalidOwner() {
        Account account = new Account();
        Customer customer = new Customer();
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        account.setCustomer(customer);
        when(accountService.getByAccountNumber("123456789012")).thenReturn(account);

        assertThrows(OwnershipException.class, () -> transactionService.getTransactionsByAccountDynamicFilter(
                "other", "123456789012", null, null, null, 0, 5));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    public void getTransactionsByAccountDynamicallyTest() {
        when(accountService.getByAccountNumber("123456789012")).thenReturn(new Account());
        when(transactionRepository.getMyTransactionsDynamically("123456789012", null, null,
                null, PageRequest.of(0, 5))).thenReturn(List.of());

        assertEquals(0, transactionService.getTransactionsByAccountDynamically(
                "123456789012", null, null, null, 0, 5).size());
        verify(transactionRepository, times(1)).getMyTransactionsDynamically("123456789012", null, null,
                null, PageRequest.of(0, 5));
    }

    @Test
    public void getAllTransactionsTest() {
        when(transactionRepository.getAllTransactions(null, null, null, PageRequest.of(0, 5)))
                .thenReturn(List.of());

        assertEquals(0, transactionService.getAllTransactions(null, null, null, 0, 5).size());
        verify(transactionRepository, times(1)).getAllTransactions(null, null, null, PageRequest.of(0, 5));
    }

    @Test
    public void getAccountSummaryTest() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456789012");
        when(accountService.getById(1L)).thenReturn(account);
        when(transactionRepository.sumAmountByAccountIdAndTypes(1L,
                List.of(TransactionType.DEPOSIT, TransactionType.LOAN_DISBURSEMENT, TransactionType.TRANSFER_IN)))
                .thenReturn(new BigDecimal("500"));
        when(transactionRepository.sumAmountByAccountIdAndTypes(1L,
                List.of(TransactionType.TRANSFER_OUT, TransactionType.WITHDRAWAL, TransactionType.LOAN_REPAYMENT)))
                .thenReturn(new BigDecimal("200"));

        AccountSummaryDto dto = transactionService.getAccountSummary(1L);

        assertEquals(new BigDecimal("500"), dto.totalInbound());
        assertEquals(new BigDecimal("200"), dto.totalOutbound());
        verify(transactionRepository, times(2)).sumAmountByAccountIdAndTypes(eq(1L), anyList());
    }

    @Test
    public void getAccountSummaryTest_NoTransactions() {
        Account account = new Account();
        when(accountService.getById(1L)).thenReturn(account);

        AccountSummaryDto dto = transactionService.getAccountSummary(1L);

        assertEquals(BigDecimal.ZERO, dto.totalInbound());
        assertEquals(BigDecimal.ZERO, dto.totalOutbound());
    }

    @Test
    public void getStatementCsvTest() {
        Customer customer = new Customer();
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        Account account = new Account();
        account.setCustomer(customer);
        when(accountService.getByAccountNumber("123456789012")).thenReturn(account);
        TransactionRespDto withdrawal = new TransactionRespDto(2L, "123456789012", TransactionType.WITHDRAWAL,
                new BigDecimal("25"), Instant.parse("2026-01-02T10:00:00Z"), "ATM", TransactionStatus.SUCCESS);
        TransactionRespDto deposit = new TransactionRespDto(1L, "123456789012", TransactionType.DEPOSIT,
                new BigDecimal("100"), Instant.parse("2026-01-01T10:00:00Z"), "Salary", TransactionStatus.SUCCESS);
        when(transactionRepository.getMyTransactionsDynamically("123456789012", null, null, null, Pageable.unpaged()))
                .thenReturn(List.of(withdrawal, deposit));

        String csv = transactionService.getStatementCsv("john", "123456789012", null, null);

        assertTrue(csv.contains("Date,Description,Type,Credit,Debit,Balance"));
        assertTrue(csv.contains("Salary,DEPOSIT,100,,100"));
        assertTrue(csv.contains("ATM,WITHDRAWAL,,25,75"));
        verify(transactionRepository, times(1)).getMyTransactionsDynamically(
                "123456789012", null, null, null, Pageable.unpaged());
    }

    @Test
    public void getStatementCsvTest_InvalidOwner() {
        Customer customer = new Customer();
        User user = new User();
        user.setUsername("john");
        customer.setUser(user);
        Account account = new Account();
        account.setCustomer(customer);
        when(accountService.getByAccountNumber("123456789012")).thenReturn(account);

        assertThrows(OwnershipException.class, () -> transactionService.getStatementCsv(
                "other", "123456789012", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)));
        verifyNoInteractions(transactionRepository);
    }
}
