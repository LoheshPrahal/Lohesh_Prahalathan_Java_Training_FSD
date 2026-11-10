package com.maverickbank.service;

import com.maverickbank.dto.request.LoanApplicationDto;
import com.maverickbank.dto.request.LoanRepaymentDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.LoanStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.model.Account;
import com.maverickbank.model.Employee;
import com.maverickbank.model.Loan;
import com.maverickbank.model.LoanApplication;
import com.maverickbank.model.LoanDisbursement;
import com.maverickbank.repository.LoanApplicationRepository;
import com.maverickbank.repository.LoanDisbursementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanApplicationServiceTest {
    @InjectMocks
    private LoanApplicationService loanApplicationService;
    @Mock
    private LoanApplicationRepository loanApplicationRepository;
    @Mock
    private LoanDisbursementRepository loanDisbursementRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private LoanService loanService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private TransactionService transactionService;

    private Account getAccount(AccountStatus status){
        Account account = new Account();
        account.setId(1L);
        account.setAccountStatus(status);
        return account;
    }

    private Loan getLoan(){
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLoanType("Home Loan");
        loan.setMinAmount(new BigDecimal("1000.00"));
        loan.setMaxAmount(new BigDecimal("50000.00"));
        return loan;
    }

    private LoanApplication getApplication(LoanStatus status, Account account){
        LoanApplication application = new LoanApplication();
        application.setId(7L);
        application.setStatus(status);
        application.setAccount(account);
        application.setLoan(getLoan());
        application.setRequestedAmount(new BigDecimal("20000.00"));
        return application;
    }

    @Test
    public void applyTest(){
        Account account = getAccount(AccountStatus.ACTIVE);
        Loan loan = getLoan();
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);
        when(loanService.getLoanById(1L)).thenReturn(loan);

        loanApplicationService.apply("john",
                new LoanApplicationDto(1L, 1L, new BigDecimal("20000.00"), "House"));

        ArgumentCaptor<LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanApplicationRepository, times(1)).save(captor.capture());
        assertEquals(LoanStatus.PENDING, captor.getValue().getStatus());
        assertEquals(account, captor.getValue().getAccount());
        assertEquals(loan, captor.getValue().getLoan());
    }

    @Test
    public void applyTest_AmountMoreThanMaximum(){
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(getAccount(AccountStatus.ACTIVE));
        when(loanService.getLoanById(1L)).thenReturn(getLoan());

        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.apply("john",
                        new LoanApplicationDto(1L, 1L, new BigDecimal("90000.00"), "House")));

        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    public void applyTest_InactiveAccount(){
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(getAccount(AccountStatus.PENDING));

        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.apply("john",
                        new LoanApplicationDto(1L, 1L, new BigDecimal("20000.00"), "House")));

        verifyNoInteractions(loanService);
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    public void reviewTest(){
        LoanApplication application = getApplication(LoanStatus.PENDING, getAccount(AccountStatus.ACTIVE));
        Employee employee = new Employee();
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));
        when(employeeService.findByUsername("emp1")).thenReturn(employee);

        loanApplicationService.review(7L, LoanStatus.APPROVED, "emp1");

        assertEquals(LoanStatus.APPROVED, application.getStatus());
        assertEquals(employee, application.getReviewedBy());
        assertNotNull(application.getReviewedDate());
        verify(loanApplicationRepository, times(1)).save(application);
    }

    @Test
    public void reviewTest_AlreadyProcessed(){
        LoanApplication application = getApplication(LoanStatus.APPROVED, getAccount(AccountStatus.ACTIVE));
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));

        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.review(7L, LoanStatus.REJECTED, "emp1"));

        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    public void reviewTest_InvalidStatus(){
        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.review(7L, LoanStatus.DISBURSED, "emp1"));

        verifyNoInteractions(loanApplicationRepository);
    }

    @Test
    public void disburseTest(){
        Account account = getAccount(AccountStatus.ACTIVE);
        LoanApplication application = getApplication(LoanStatus.APPROVED, account);
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));

        loanApplicationService.disburse(7L);

        verify(loanDisbursementRepository, times(1)).save(any(LoanDisbursement.class));
        verify(accountService, times(1)).credit(account, new BigDecimal("20000.00"));
        verify(transactionService, times(1)).recordTransaction(eq(account), eq(TransactionType.LOAN_DISBURSEMENT),
                eq(new BigDecimal("20000.00")), anyString(), isNull());
        assertEquals(LoanStatus.DISBURSED, application.getStatus());
        // Repayment - the full amount is to be paid back
        assertEquals(new BigDecimal("20000.00"), application.getRemainingAmount());
    }

    @Test
    public void disburseTest_NotApproved(){
        LoanApplication application = getApplication(LoanStatus.PENDING, getAccount(AccountStatus.ACTIVE));
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));

        assertThrows(InvalidCallException.class, () -> loanApplicationService.disburse(7L));

        verifyNoInteractions(loanDisbursementRepository);
        verifyNoInteractions(accountService);
    }

    // Repayment tests
    @Test
    public void repayTest(){
        Account account = getAccount(AccountStatus.ACTIVE);
        LoanApplication application = getApplication(LoanStatus.DISBURSED, account);
        application.setRemainingAmount(new BigDecimal("20000.00"));
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        loanApplicationService.repay("john", new LoanRepaymentDto(7L, new BigDecimal("5000.00")));

        verify(accountService, times(1)).debit(account, new BigDecimal("5000.00"));
        verify(transactionService, times(1)).recordTransaction(eq(account), eq(TransactionType.LOAN_REPAYMENT),
                eq(new BigDecimal("5000.00")), anyString(), isNull());
        assertEquals(new BigDecimal("15000.00"), application.getRemainingAmount());
        verify(loanApplicationRepository, times(1)).save(application);
    }

    @Test
    public void repayTest_MoreThanRemaining(){
        Account account = getAccount(AccountStatus.ACTIVE);
        LoanApplication application = getApplication(LoanStatus.DISBURSED, account);
        application.setRemainingAmount(new BigDecimal("1000.00"));
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.repay("john", new LoanRepaymentDto(7L, new BigDecimal("1500.00"))));

        verify(accountService, never()).debit(any(Account.class), any(BigDecimal.class));
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    public void closeTest(){
        LoanApplication application = getApplication(LoanStatus.DISBURSED, getAccount(AccountStatus.ACTIVE));
        application.setRemainingAmount(new BigDecimal("0.00"));
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));

        loanApplicationService.close(7L, "emp1");

        assertEquals(LoanStatus.CLOSED, application.getStatus());
        verify(loanApplicationRepository, times(1)).save(application);
    }

    @Test
    public void closeTest_AmountRemaining(){
        LoanApplication application = getApplication(LoanStatus.DISBURSED, getAccount(AccountStatus.ACTIVE));
        application.setRemainingAmount(new BigDecimal("100.00"));
        when(loanApplicationRepository.findById(7L)).thenReturn(Optional.of(application));

        assertThrows(InvalidCallException.class, () -> loanApplicationService.close(7L, "emp1"));

        assertEquals(LoanStatus.DISBURSED, application.getStatus());
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }
}
