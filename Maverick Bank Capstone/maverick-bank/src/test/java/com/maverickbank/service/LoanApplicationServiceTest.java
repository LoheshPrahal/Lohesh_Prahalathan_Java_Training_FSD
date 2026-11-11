package com.maverickbank.service;

import com.maverickbank.dto.request.LoanApplicationDto;
import com.maverickbank.dto.request.LoanRepaymentDto;
import com.maverickbank.dto.response.LoanApplicationRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.LoanStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Account;
import com.maverickbank.model.Customer;
import com.maverickbank.model.Employee;
import com.maverickbank.model.Loan;
import com.maverickbank.model.LoanApplication;
import com.maverickbank.model.LoanDisbursement;
import com.maverickbank.repository.LoanApplicationRepository;
import com.maverickbank.repository.LoanDisbursementRepository;
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
public class LoanApplicationServiceTest {
    @InjectMocks
    private LoanApplicationService loanApplicationService;
    @Mock
    private AccountService accountService;
    @Mock
    private LoanService loanService;
    @Mock
    private LoanApplicationRepository loanApplicationRepository;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private LoanDisbursementRepository loanDisbursementRepository;
    @Mock
    private TransactionService transactionService;

    @Test
    public void applyTest() {
        LoanApplicationDto dto = new LoanApplicationDto(1L, 2L, new BigDecimal("5000"), "Education");
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        Loan loan = new Loan();
        loan.setMinAmount(new BigDecimal("1000"));
        loan.setMaxAmount(new BigDecimal("10000"));
        when(accountService.getOwnedAccount(2L, "john")).thenReturn(account);
        when(loanService.getLoanById(1L)).thenReturn(loan);

        loanApplicationService.apply("john", dto);

        verify(accountService, times(1)).getOwnedAccount(2L, "john");
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    public void applyTest_InactiveAccount() {
        LoanApplicationDto dto = new LoanApplicationDto(1L, 2L, new BigDecimal("5000"), "Education");
        Account account = new Account();
        account.setAccountStatus(AccountStatus.CLOSED);
        when(accountService.getOwnedAccount(2L, "john")).thenReturn(account);

        assertThrows(InvalidCallException.class, () -> loanApplicationService.apply("john", dto));
        verify(loanApplicationRepository, times(0)).save(any(LoanApplication.class));
    }

    @Test
    public void applyTest_InvalidAmount() {
        LoanApplicationDto dto = new LoanApplicationDto(1L, 2L, new BigDecimal("500"), "Education");
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        Loan loan = new Loan();
        loan.setMinAmount(new BigDecimal("1000"));
        loan.setMaxAmount(new BigDecimal("10000"));
        when(accountService.getOwnedAccount(2L, "john")).thenReturn(account);
        when(loanService.getLoanById(1L)).thenReturn(loan);

        assertThrows(InvalidCallException.class, () -> loanApplicationService.apply("john", dto));
        verify(loanApplicationRepository, times(0)).save(any(LoanApplication.class));
    }

    @Test
    public void getMyApplicationsTest() {
        LoanApplicationRespDto dto = new LoanApplicationRespDto(1L, "Personal Loan", 2L, "123", "John",
                new BigDecimal("5000"), 10.0, 12, "Education", null, LoanStatus.PENDING, null, null);
        when(loanApplicationRepository.getByCustomerUsername("john")).thenReturn(List.of(dto));

        assertEquals(1, loanApplicationService.getMyApplications("john").size());
        verify(loanApplicationRepository, times(1)).getByCustomerUsername("john");
    }

    @Test
    public void getMyAvailedLoansTest() {
        when(loanApplicationRepository.getByCustomerUsernameAndStatusIn("john",
                List.of(LoanStatus.DISBURSED, LoanStatus.CLOSED))).thenReturn(List.of());

        assertEquals(0, loanApplicationService.getMyAvailedLoans("john").size());
        verify(loanApplicationRepository, times(1)).getByCustomerUsernameAndStatusIn("john",
                List.of(LoanStatus.DISBURSED, LoanStatus.CLOSED));
    }

    @Test
    public void getMyLoanByIdTest() {
        LoanApplication application = new LoanApplication();
        application.setId(1L);
        application.setRequestedAmount(new BigDecimal("5000"));
        application.setStatus(LoanStatus.DISBURSED);
        Account account = new Account();
        account.setId(2L);
        account.setCustomer(new Customer());
        application.setAccount(account);
        Loan loan = new Loan();
        loan.setLoanType("Personal Loan");
        application.setLoan(loan);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        assertEquals(1L, loanApplicationService.getMyLoanById(1L, "john").applicationId());
        verify(accountService, times(1)).getOwnedAccount(2L, "john");
        verify(loanApplicationRepository, times(1)).findById(1L);
    }

    @Test
    public void getFullyPaidLoansTest() {
        when(loanApplicationRepository.getFullyPaidLoans(LoanStatus.DISBURSED)).thenReturn(List.of());

        assertEquals(0, loanApplicationService.getFullyPaidLoans().size());
        verify(loanApplicationRepository, times(1)).getFullyPaidLoans(LoanStatus.DISBURSED);
    }

    @Test
    public void getApplicationsByStatusTest() {
        when(loanApplicationRepository.getByStatus(LoanStatus.PENDING)).thenReturn(List.of());

        assertEquals(0, loanApplicationService.getApplicationsByStatus(LoanStatus.PENDING).size());
        verify(loanApplicationRepository, times(1)).getByStatus(LoanStatus.PENDING);
    }

    @Test
    public void reviewTest() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.PENDING);
        Employee employee = new Employee();
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(employeeService.findByUsername("john")).thenReturn(employee);

        loanApplicationService.review(1L, LoanStatus.APPROVED, "john");

        assertEquals(LoanStatus.APPROVED, application.getStatus());
        assertEquals(employee, application.getReviewedBy());
        assertNotNull(application.getReviewedDate());
        verify(loanApplicationRepository, times(1)).save(application);
    }

    @Test
    public void reviewTest_InvalidStatus() {
        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.review(1L, LoanStatus.DISBURSED, "john"));
        verify(loanApplicationRepository, times(0)).save(any(LoanApplication.class));
    }

    @Test
    public void reviewTest_AlreadyProcessed() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.APPROVED);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.review(1L, LoanStatus.REJECTED, "john"));
        verify(loanApplicationRepository, times(0)).save(any(LoanApplication.class));
    }

    @Test
    public void disburseTest() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.APPROVED);
        application.setRequestedAmount(new BigDecimal("5000"));
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        application.setAccount(account);
        Loan loan = new Loan();
        loan.setLoanType("Personal Loan");
        application.setLoan(loan);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        loanApplicationService.disburse(1L);

        assertEquals(LoanStatus.DISBURSED, application.getStatus());
        assertEquals(new BigDecimal("5000"), application.getRemainingAmount());
        verify(loanDisbursementRepository, times(1)).save(any(LoanDisbursement.class));
        verify(accountService, times(1)).credit(account, new BigDecimal("5000"));
        verify(transactionService, times(1)).recordTransaction(eq(account), eq(TransactionType.LOAN_DISBURSEMENT),
                eq(new BigDecimal("5000")), anyString(), isNull());
        verify(loanApplicationRepository, times(1)).save(application);
    }

    @Test
    public void disburseTest_Invalid() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.PENDING);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        assertThrows(InvalidCallException.class, () -> loanApplicationService.disburse(1L));
        verify(loanDisbursementRepository, times(0)).save(any(LoanDisbursement.class));
    }

    @Test
    public void repayTest() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.DISBURSED);
        application.setRemainingAmount(new BigDecimal("5000"));
        Account account = new Account();
        account.setId(2L);
        account.setAccountStatus(AccountStatus.ACTIVE);
        application.setAccount(account);
        Loan loan = new Loan();
        loan.setLoanType("Personal Loan");
        application.setLoan(loan);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(accountService.getOwnedAccount(2L, "john")).thenReturn(account);

        loanApplicationService.repay("john", new LoanRepaymentDto(1L, new BigDecimal("1000")));

        assertEquals(new BigDecimal("4000"), application.getRemainingAmount());
        verify(accountService, times(1)).debit(account, new BigDecimal("1000"));
        verify(transactionService, times(1)).recordTransaction(eq(account), eq(TransactionType.LOAN_REPAYMENT),
                eq(new BigDecimal("1000")), anyString(), isNull());
        verify(loanApplicationRepository, times(1)).save(application);
    }

    @Test
    public void repayTest_InvalidAmount() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.DISBURSED);
        application.setRemainingAmount(new BigDecimal("1000"));
        Account account = new Account();
        account.setId(2L);
        account.setAccountStatus(AccountStatus.ACTIVE);
        application.setAccount(account);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(accountService.getOwnedAccount(2L, "john")).thenReturn(account);

        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.repay("john", new LoanRepaymentDto(1L, new BigDecimal("2000"))));
        verify(accountService, times(0)).debit(any(Account.class), any(BigDecimal.class));
    }

    @Test
    public void repayTest_AlreadyPaid() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.DISBURSED);
        application.setRemainingAmount(BigDecimal.ZERO);
        Account account = new Account();
        account.setId(2L);
        account.setAccountStatus(AccountStatus.ACTIVE);
        application.setAccount(account);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(accountService.getOwnedAccount(2L, "john")).thenReturn(account);

        assertThrows(InvalidCallException.class,
                () -> loanApplicationService.repay("john", new LoanRepaymentDto(1L, new BigDecimal("100"))));
        verify(accountService, times(0)).debit(any(Account.class), any(BigDecimal.class));
    }

    @Test
    public void closeTest() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.DISBURSED);
        application.setRemainingAmount(BigDecimal.ZERO);
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        loanApplicationService.close(1L, "john");

        assertEquals(LoanStatus.CLOSED, application.getStatus());
        verify(loanApplicationRepository, times(1)).save(application);
    }

    @Test
    public void closeTest_RemainingAmount() {
        LoanApplication application = new LoanApplication();
        application.setStatus(LoanStatus.DISBURSED);
        application.setRemainingAmount(new BigDecimal("100"));
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        assertThrows(InvalidCallException.class, () -> loanApplicationService.close(1L, "john"));
        verify(loanApplicationRepository, times(0)).save(any(LoanApplication.class));
    }

    @Test
    public void getMyLoanByIdTest_Invalid() {
        when(loanApplicationRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanApplicationService.getMyLoanById(5L, "john"));
        verify(loanApplicationRepository, times(1)).findById(5L);
    }
}
