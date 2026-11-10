package com.maverickbank.controller;

import com.maverickbank.dto.request.LoanApplicationDto;
import com.maverickbank.dto.request.LoanRepaymentDto;
import com.maverickbank.dto.response.LoanApplicationRespDto;
import com.maverickbank.enums.LoanStatus;
import com.maverickbank.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    @PostMapping("/api/loan/apply")
    public void apply(Principal principal, @Valid @RequestBody LoanApplicationDto dto){
        loanApplicationService.apply(principal.getName(), dto);
    }

    @GetMapping("/api/loan/application/my")
    public List<LoanApplicationRespDto> getMyApplications(Principal principal){
        return loanApplicationService.getMyApplications(principal.getName());
    }

    // API version 1 - fetch the loans of the logged-in customer by the username (disbursed and closed, with the remaining amount)
    @GetMapping("/api/v1/loan/application/availed")
    public List<LoanApplicationRespDto> getMyAvailedLoans(Principal principal){
        return loanApplicationService.getMyAvailedLoans(principal.getName());
    }

    // API version 2 - fetch one loan of the logged-in customer by the id
    @GetMapping("/api/v2/loan/application/{applicationId}")
    public LoanApplicationRespDto getMyLoanById(@PathVariable long applicationId, Principal principal){
        return loanApplicationService.getMyLoanById(applicationId, principal.getName());
    }

    // Repayment - the customer repays the loan, the amount is the choice of the customer
    @PostMapping("/api/loan/application/repay")
    public void repay(Principal principal, @Valid @RequestBody LoanRepaymentDto dto){
        loanApplicationService.repay(principal.getName(), dto);
    }

    @GetMapping("/api/loan/application/pending")
    public List<LoanApplicationRespDto> getPendingApplications(){
        return loanApplicationService.getApplicationsByStatus(LoanStatus.PENDING);
    }

    @GetMapping("/api/loan/application/approved")
    public List<LoanApplicationRespDto> getApprovedApplications(){
        return loanApplicationService.getApplicationsByStatus(LoanStatus.APPROVED);
    }

    @PutMapping("/api/loan/application/review/{applicationId}")
    public void review(@PathVariable long applicationId, @RequestParam("status") LoanStatus status, Principal principal){
        loanApplicationService.review(applicationId, status, principal.getName());
    }

    @PutMapping("/api/loan/application/disburse/{applicationId}")
    public void disburse(@PathVariable long applicationId){
        loanApplicationService.disburse(applicationId);
    }

    // Repayment - loans which are fully repaid, waiting for the employee to close them
    @GetMapping("/api/loan/application/repaid")
    public List<LoanApplicationRespDto> getFullyPaidLoans(){
        return loanApplicationService.getFullyPaidLoans();
    }

    // Repayment - the employee closes a fully repaid loan
    @PutMapping("/api/loan/application/close/{applicationId}")
    public void close(@PathVariable long applicationId, Principal principal){
        loanApplicationService.close(applicationId, principal.getName());
    }
}
