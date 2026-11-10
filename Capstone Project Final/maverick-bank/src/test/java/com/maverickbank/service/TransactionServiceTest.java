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
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    private Account getAccount(long accountId, long customerId, String accountNumber, AccountStatus status){
        Customer customer = new Customer();
        customer.setId(customerId);

        Account account = new Account();
        account.setId(accountId);
        account.setAccountNumber(accountNumber);
        account.setCustomer(customer);
        account.setAccountStatus(status);
        return account;
    }

    @Test
    public void depositTest(){
        Account account = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);
        BigDecimal amount = new BigDecimal("500.00");
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        transactionService.deposit("john", new TransactionDto(1L, amount, "salary"));

        verify(accountService, times(1)).credit(account, amount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void withdrawTest(){
        Account account = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);
        BigDecimal amount = new BigDecimal("200.00");
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        transactionService.withdraw("john", new TransactionDto(1L, amount, null));

        verify(accountService, times(1)).debit(account, amount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void withdrawTest_InactiveAccount(){
        Account account = getAccount(1L, 1L, "111111111111", AccountStatus.PENDING);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        assertThrows(InvalidCallException.class,
                () -> transactionService.withdraw("john", new TransactionDto(1L, new BigDecimal("200.00"), null)));

        verify(accountService, never()).debit(any(Account.class), any(BigDecimal.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    public void transferTest(){
        Account source = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);
        Account destination = getAccount(2L, 2L, "222222222222", AccountStatus.ACTIVE);
        BigDecimal amount = new BigDecimal("300.00");

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setAccountName("Peter");
        beneficiary.setAccountNumber("222222222222");
        beneficiary.setCustomer(source.getCustomer());

        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(5L)).thenReturn(Optional.of(beneficiary));
        when(accountService.getByAccountNumber("222222222222")).thenReturn(destination);

        transactionService.transfer("john", new TransferDto(1L, 5L, amount, null));

        verify(accountService, times(1)).debit(source, amount);
        verify(accountService, times(1)).credit(destination, amount);
        // one entry for each account
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    public void transferTest_BeneficiaryOfAnotherCustomer(){
        Account source = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);

        Customer another = new Customer();
        another.setId(9L);
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setCustomer(another);

        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(5L)).thenReturn(Optional.of(beneficiary));

        assertThrows(OwnershipException.class,
                () -> transactionService.transfer("john", new TransferDto(1L, 5L, new BigDecimal("300.00"), null)));

        verify(accountService, never()).debit(any(Account.class), any(BigDecimal.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    public void transferTest_SameAccount(){
        Account source = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setAccountNumber("111111111111");
        beneficiary.setCustomer(source.getCustomer());

        when(accountService.getOwnedAccount(1L, "john")).thenReturn(source);
        when(beneficiaryRepository.findById(5L)).thenReturn(Optional.of(beneficiary));
        when(accountService.getByAccountNumber("111111111111")).thenReturn(source);

        assertThrows(InvalidCallException.class,
                () -> transactionService.transfer("john", new TransferDto(1L, 5L, new BigDecimal("300.00"), null)));

        verifyNoInteractions(transactionRepository);
    }

    @Test
    public void getAccountSummaryTest(){
        Account account = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);
        when(accountService.getById(1L)).thenReturn(account);

        when(transactionRepository.sumAmountByAccountIdAndTypes(eq(1L),
                argThat((List<TransactionType> types) -> types != null && types.contains(TransactionType.DEPOSIT))))
                .thenReturn(new BigDecimal("1000.00"));
        when(transactionRepository.sumAmountByAccountIdAndTypes(eq(1L),
                argThat((List<TransactionType> types) -> types != null && types.contains(TransactionType.WITHDRAWAL))))
                .thenReturn(null);

        AccountSummaryDto summary = transactionService.getAccountSummary(1L);

        assertEquals(new BigDecimal("1000.00"), summary.totalInbound());
        assertEquals(BigDecimal.ZERO, summary.totalOutbound());
        assertEquals("111111111111", summary.accountNumber());
    }

    // Statement test
    @Test
    public void getStatementCsvTest(){
        User user = new User();
        user.setUsername("john");
        Account account = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);
        account.getCustomer().setUser(user);
        when(accountService.getByAccountNumber("111111111111")).thenReturn(account);
        // the repository gives the latest transaction first
        when(transactionRepository.getMyTransactionsDynamically(eq("111111111111"), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(List.of(
                        new TransactionRespDto(2L, "111111111111", TransactionType.LOAN_REPAYMENT, new BigDecimal("300.00"),
                                Instant.parse("2026-01-02T10:00:00Z"), "Loan repayment - Home Loan", TransactionStatus.SUCCESS),
                        new TransactionRespDto(1L, "111111111111", TransactionType.DEPOSIT, new BigDecimal("1000.00"),
                                Instant.parse("2026-01-01T10:00:00Z"), "Cash, \"salary\"", TransactionStatus.SUCCESS)));

        String csv = transactionService.getStatementCsv("john", "111111111111", null, null);

        String[] lines = csv.split("\n");
        assertEquals("Date,Description,Type,Credit,Debit,Balance", lines[0]);
        assertTrue(lines[1].endsWith("\"Cash, \"\"salary\"\"\",DEPOSIT,1000.00,,1000.00"));
        assertTrue(lines[2].endsWith("\"Loan repayment - Home Loan\",LOAN_REPAYMENT,,300.00,700.00"));
    }

    @Test
    public void getStatementCsvTest_NotOwnAccount(){
        User user = new User();
        user.setUsername("mary");
        Account account = getAccount(1L, 1L, "111111111111", AccountStatus.ACTIVE);
        account.getCustomer().setUser(user);
        when(accountService.getByAccountNumber("111111111111")).thenReturn(account);

        assertThrows(OwnershipException.class,
                () -> transactionService.getStatementCsv("john", "111111111111", null, null));

        verifyNoInteractions(transactionRepository);
    }
}
