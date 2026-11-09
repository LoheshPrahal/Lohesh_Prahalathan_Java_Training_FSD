package com.maverickbank.service;

import com.maverickbank.dto.request.LoanApplicationDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
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
    }

    public List<LoanApplicationRespDto> getMyApplications(String customerUsername) {
        return loanApplicationRepository.getByCustomerUsername(customerUsername);
    }

    // Loans which are already disbursed to the customer
    public List<LoanApplicationRespDto> getMyAvailedLoans(String customerUsername) {
        return loanApplicationRepository.getByCustomerUsernameAndStatus(customerUsername, LoanStatus.DISBURSED);
    }

    public List<LoanApplicationRespDto> getApplicationsByStatus(LoanStatus status) {
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
    }

    // Approved loans are disbursed to the customer's account
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

        loanApplication.setStatus(LoanStatus.DISBURSED);
        loanApplicationRepository.save(loanApplication);
    }

    private LoanApplication getById(Long applicationId) {
        return loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application with id " + applicationId + " not found"));
    }
}
