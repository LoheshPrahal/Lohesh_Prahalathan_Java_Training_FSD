package com.maverickbank.service;

import com.maverickbank.dto.request.AccountClosureDto;
import com.maverickbank.dto.response.AccountClosureRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.RequestStatus;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Account;
import com.maverickbank.model.AccountClosureRequest;
import com.maverickbank.model.Employee;
import com.maverickbank.repository.AccountClosureRequestRepository;
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
public class AccountClosureRequestServiceTest {
    @InjectMocks
    private AccountClosureRequestService accountClosureRequestService;
    @Mock
    private AccountService accountService;
    @Mock
    private AccountClosureRequestRepository accountClosureRequestRepository;
    @Mock
    private EmployeeService employeeService;

    @Test
    public void addTest() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        accountClosureRequestService.add("john", 1L, new AccountClosureDto("Not needed"));

        verify(accountClosureRequestRepository, times(1)).countByAccountIdAndStatus(1L, RequestStatus.PENDING);
        verify(accountClosureRequestRepository, times(1)).save(any(AccountClosureRequest.class));
    }

    @Test
    public void addTest_InactiveAccount() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.CLOSED);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);

        assertThrows(InvalidCallException.class,
                () -> accountClosureRequestService.add("john", 1L, new AccountClosureDto("Not needed")));
        verify(accountClosureRequestRepository, times(0)).save(any(AccountClosureRequest.class));
    }

    @Test
    public void addTest_PendingRequest() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        when(accountService.getOwnedAccount(1L, "john")).thenReturn(account);
        when(accountClosureRequestRepository.countByAccountIdAndStatus(1L, RequestStatus.PENDING)).thenReturn(1L);

        assertThrows(InvalidCallException.class,
                () -> accountClosureRequestService.add("john", 1L, new AccountClosureDto("Not needed")));
        verify(accountClosureRequestRepository, times(0)).save(any(AccountClosureRequest.class));
    }

    @Test
    public void checkBalanceIsZeroTest() {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);

        assertDoesNotThrow(() -> accountClosureRequestService.checkBalanceIsZero(account));
    }

    @Test
    public void checkBalanceIsZeroTest_Invalid() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));

        assertThrows(InvalidCallException.class, () -> accountClosureRequestService.checkBalanceIsZero(account));
    }

    @Test
    public void getPendingRequestsTest() {
        AccountClosureRespDto dto = new AccountClosureRespDto(1L, 1L, "123", "John", "Not needed", null, RequestStatus.PENDING);
        when(accountClosureRequestRepository.getByStatus(RequestStatus.PENDING)).thenReturn(List.of(dto));

        assertEquals(1, accountClosureRequestService.getPendingRequests().size());
        verify(accountClosureRequestRepository, times(1)).getByStatus(RequestStatus.PENDING);
    }

    @Test
    public void approveTest() {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        AccountClosureRequest request = new AccountClosureRequest();
        request.setStatus(RequestStatus.PENDING);
        request.setAccount(account);
        Employee employee = new Employee();
        when(accountClosureRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(employeeService.findByUsername("john")).thenReturn(employee);

        accountClosureRequestService.approve(1L, "john");

        assertEquals(RequestStatus.APPROVED, request.getStatus());
        assertEquals(employee, request.getProcessedBy());
        verify(accountService, times(1)).closeAccount(account);
        verify(accountClosureRequestRepository, times(1)).save(request);
    }

    @Test
    public void rejectTest() {
        AccountClosureRequest request = new AccountClosureRequest();
        request.setStatus(RequestStatus.PENDING);
        Employee employee = new Employee();
        when(accountClosureRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(employeeService.findByUsername("john")).thenReturn(employee);

        accountClosureRequestService.reject(1L, "john");

        assertEquals(RequestStatus.REJECTED, request.getStatus());
        verify(accountClosureRequestRepository, times(1)).save(request);
    }

    @Test
    public void approveTest_InvalidRequest() {
        when(accountClosureRequestRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountClosureRequestService.approve(5L, "john"));
        verify(accountClosureRequestRepository, times(1)).findById(5L);
    }

    @Test
    public void rejectTest_AlreadyProcessed() {
        AccountClosureRequest request = new AccountClosureRequest();
        request.setStatus(RequestStatus.APPROVED);
        when(accountClosureRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(InvalidCallException.class, () -> accountClosureRequestService.reject(1L, "john"));
        verify(accountClosureRequestRepository, times(0)).save(any(AccountClosureRequest.class));
    }
}
