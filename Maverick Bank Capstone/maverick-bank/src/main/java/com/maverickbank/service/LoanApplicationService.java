package com.maverickbank.service;

import com.maverickbank.dto.request.LoanApplicationDto;
import com.maverickbank.dto.request.LoanRepaymentDto;
import com.maverickbank.dto.response.LoanApplicationRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.LoanStatus;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.LoanApplicationMapper;
import com.maverickbank.model.*;
import com.maverickbank.repository.LoanApplicationRepository;
import com.maverickbank.repository.LoanDisbursementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class LoanApplicationService {
    private final AccountService accountService;
    private final LoanService loanService;
    private final LoanApplicationRepository loanApplicationRepository;
    private final EmployeeService employeeService;
    private final LoanDisbursementRepository loanDisbursementRepository;
    private final TransactionService transactionService;

    public void apply(String customerUsername, LoanApplicationDto dto) {
        Account account = accountService.getOwnedAccount(dto.accountId(), customerUsername);

        if(account.getAccountStatus() != AccountStatus.ACTIVE){
            throw new InvalidCallException("Loan can be applied only with an active account");
        }

        Loan loan = loanService.getLoanById(dto.loanId());

        if(dto.requestedAmount().compareTo(loan.getMinAmount()) < 0 || dto.requestedAmount().compareTo(loan.getMaxAmount()) > 0){
            throw new InvalidCallException("Requested amount should be between "
                    + loan.getMinAmount() + " and " + loan.getMaxAmount());
        }

        LoanApplication loanApplication = LoanApplicationMapper.convertDtoToEntity(dto);
        loanApplication.setLoan(loan);
        loanApplication.setAccount(account);
        loanApplication.setStatus(LoanStatus.PENDING);

        loanApplicationRepository.save(loanApplication);

        // Log: the customer has applied for a loan
        log.info("Loan id {} applied by customer {}", dto.loanId(), customerUsername);
    }

    public List<LoanApplicationRespDto> getMyApplications(String customerUsername) {
        // Log: loan applications of the customer are listed
        log.info("Fetching loan applications of customer {}", customerUsername);

        return loanApplicationRepository.getByCustomerUsername(customerUsername);
    }

    public List<LoanApplicationRespDto> getMyAvailedLoans(String customerUsername) {
        // Log: loans already disbursed to the customer are listed
        log.info("Fetching availed loans of customer {}", customerUsername);

        // Repayment - the closed loans are also listed, so the customer sees them after the employee closes
        return loanApplicationRepository.getByCustomerUsernameAndStatusIn(customerUsername, List.of(LoanStatus.DISBURSED, LoanStatus.CLOSED));
    }

    // Repayment - one loan of the customer, found with the id (used by API version 2)
    public LoanApplicationRespDto getMyLoanById(Long applicationId, String customerUsername) {
        // Log: customer opens one loan
        log.info("Fetching loan application {} of customer {}", applicationId, customerUsername);

        LoanApplication loanApplication = getById(applicationId);

        // Only the owner of the account of the loan can see it
        accountService.getOwnedAccount(loanApplication.getAccount().getId(), customerUsername);

        return LoanApplicationMapper.convertEntityToDto(loanApplication);
    }

    // Repayment - loans with the remaining amount zero, the employee closes them
    public List<LoanApplicationRespDto> getFullyPaidLoans() {
        // Log: fully repaid loans are listed for the employee
        log.info("Fetching the fully repaid loans");

        return loanApplicationRepository.getFullyPaidLoans(LoanStatus.DISBURSED);
    }

    public List<LoanApplicationRespDto> getApplicationsByStatus(LoanStatus status) {
        // Log: loan applications are listed for the employee
        log.info("Fetching loan applications with status {}", status);

        return loanApplicationRepository.getByStatus(status);
    }

    public void review(Long applicationId, LoanStatus status, String employeeUsername) {
        if(status != LoanStatus.APPROVED && status != LoanStatus.REJECTED){
            throw new InvalidCallException("Loan application can only be approved or rejected");
        }

        LoanApplication loanApplication = getById(applicationId);

        if(loanApplication.getStatus() != LoanStatus.PENDING){
            throw new InvalidCallException("Loan application is already processed");
        }

        Employee employee = employeeService.findByUsername(employeeUsername);

        loanApplication.setStatus(status);
        loanApplication.setReviewedBy(employee);
        loanApplication.setReviewedDate(LocalDate.now());

        loanApplicationRepository.save(loanApplication);

        // Log: the employee has reviewed the loan application
        log.info("Loan application {} is {} by employee {}", applicationId, status, employeeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public void disburse(Long applicationId) {
        LoanApplication loanApplication = getById(applicationId);

        if(loanApplication.getStatus() != LoanStatus.APPROVED){
            throw new InvalidCallException("Only approved loan applications can be disbursed");
        }

        Account account = loanApplication.getAccount();
        if(account.getAccountStatus() != AccountStatus.ACTIVE){
            throw new InvalidCallException("Loan cannot be disbursed to an inactive account");
        }

        LoanDisbursement loanDisbursement = new LoanDisbursement();
        loanDisbursement.setLoanApplication(loanApplication);
        loanDisbursement.setDisbursementAmount(loanApplication.getRequestedAmount());
        loanDisbursementRepository.save(loanDisbursement);

        accountService.credit(account, loanApplication.getRequestedAmount());
        transactionService.recordTransaction(account, TransactionType.LOAN_DISBURSEMENT,
                loanApplication.getRequestedAmount(), "Loan disbursement - " + loanApplication.getLoan().getLoanType(), null);

        // Repayment - the full amount is to be paid back
        loanApplication.setRemainingAmount(loanApplication.getRequestedAmount());

        loanApplication.setStatus(LoanStatus.DISBURSED);
        loanApplicationRepository.save(loanApplication);

        // Log: the loan amount is credited to the account
        log.info("Loan application {} is disbursed", applicationId);
    }

    // Repayment - the customer repays any amount (not more than the remaining amount) from the account of the loan
    @Transactional(rollbackFor = Exception.class)
    public void repay(String customerUsername, LoanRepaymentDto dto) {
        LoanApplication loanApplication = getById(dto.applicationId());
        Account account = accountService.getOwnedAccount(loanApplication.getAccount().getId(), customerUsername);

        if(loanApplication.getStatus() != LoanStatus.DISBURSED){
            throw new InvalidCallException("Only disbursed loans can be repaid");
        }

        if(account.getAccountStatus() != AccountStatus.ACTIVE){
            throw new InvalidCallException("Loan can be repaid only from an active account");
        }

        BigDecimal remaining = loanApplication.getRemainingAmount();

        if(remaining.compareTo(BigDecimal.ZERO) == 0){
            throw new InvalidCallException("Loan is already fully repaid");
        }

        if(dto.amount().compareTo(remaining) > 0){
            throw new InvalidCallException("Repayment amount cannot be more than the remaining amount " + remaining);
        }

        accountService.debit(account, dto.amount());
        transactionService.recordTransaction(account, TransactionType.LOAN_REPAYMENT,
                dto.amount(), "Loan repayment - " + loanApplication.getLoan().getLoanType(), null);

        loanApplication.setRemainingAmount(remaining.subtract(dto.amount()));
        loanApplicationRepository.save(loanApplication);

        // Log: the customer has repaid a part (or all) of the loan
        log.info("Loan application {} repaid {} by customer {}", dto.applicationId(), dto.amount(), customerUsername);
    }

    // Repayment - the employee closes the loan, only when nothing is left to pay
    public void close(Long applicationId, String employeeUsername) {
        LoanApplication loanApplication = getById(applicationId);

        if(loanApplication.getStatus() != LoanStatus.DISBURSED){
            throw new InvalidCallException("Only disbursed loans can be closed");
        }

        if(loanApplication.getRemainingAmount().compareTo(BigDecimal.ZERO) != 0){
            throw new InvalidCallException("Loan cannot be closed until the full amount is repaid");
        }

        loanApplication.setStatus(LoanStatus.CLOSED);
        loanApplicationRepository.save(loanApplication);

        // Log: the employee has closed the loan
        log.info("Loan application {} is closed by employee {}", applicationId, employeeUsername);
    }

    private LoanApplication getById(Long applicationId) {
        return loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application with id " + applicationId + " not found"));
    }
}
