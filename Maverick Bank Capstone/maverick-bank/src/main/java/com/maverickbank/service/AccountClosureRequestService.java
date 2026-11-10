package com.maverickbank.service;

import com.maverickbank.dto.request.AccountClosureDto;
import com.maverickbank.dto.response.AccountClosureRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.RequestStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.AccountClosureRequestMapper;
import com.maverickbank.model.Account;
import com.maverickbank.model.AccountClosureRequest;
import com.maverickbank.model.Employee;
import com.maverickbank.repository.AccountClosureRequestRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class AccountClosureRequestService {
    private final AccountService accountService;
    private final AccountClosureRequestRepository accountClosureRequestRepository;
    private final EmployeeService employeeService;

    public void add(String username, Long accountId, @Valid AccountClosureDto dto) {
        Account account = accountService.getOwnedAccount(accountId, username);

        if(account.getAccountStatus() != AccountStatus.ACTIVE)
            throw new InvalidCallException("Only active accounts can be closed!");

        checkBalanceIsZero(account);

        if(accountClosureRequestRepository.countByAccountIdAndStatus(accountId, RequestStatus.PENDING) > 0){
            throw new InvalidCallException("A closure request is already pending!");
        }

        AccountClosureRequest request = AccountClosureRequestMapper.convertDtoToEntity(dto);
        request.setAccount(account);
        request.setStatus(RequestStatus.PENDING);

        accountClosureRequestRepository.save(request);

        // Log: the customer has sent a closure request
        log.info("Closure request sent for account id {} by customer {}", accountId, username);
    }

    public List<AccountClosureRespDto> getPendingRequests() {
        // Log: closure requests waiting for the employee are listed
        log.info("Fetching the pending account closure requests");
        return accountClosureRequestRepository.getByStatus(RequestStatus.PENDING);
    }

    @Transactional(rollbackFor = Exception.class)
    public void approve(Long requestId, String name) {
        AccountClosureRequest request = getPendingRequest(requestId);

        checkBalanceIsZero(request.getAccount());

        Employee employee = employeeService.findByUsername(name);

        accountService.closeAccount(request.getAccount());

        request.setStatus(RequestStatus.APPROVED);
        request.setProcessedBy(employee);
        accountClosureRequestRepository.save(request);

        // Log: the employee has approved the closure request
        log.info("Closure request {} approved by employee {}", requestId, name);
    }

    public void reject(Long requestId, String employeeUsername) {
        AccountClosureRequest request = getPendingRequest(requestId);
        Employee employee = employeeService.findByUsername(employeeUsername);

        request.setStatus(RequestStatus.REJECTED);
        request.setProcessedBy(employee);
        accountClosureRequestRepository.save(request);

        // Log: the employee has rejected the closure request
        log.info("Closure request {} rejected by employee {}", requestId, employeeUsername);
    }

    private AccountClosureRequest getPendingRequest(Long requestId) {
        AccountClosureRequest request = accountClosureRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Closure request with id " + requestId + " not found!"));

        if(request.getStatus() != RequestStatus.PENDING){
            throw new InvalidCallException("Closure request is already processed!");
        }

        return request;
    }

    public void checkBalanceIsZero(Account account){
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0)
            throw new InvalidCallException("Withdraw the remaining amount before closing the account!");    }
}
