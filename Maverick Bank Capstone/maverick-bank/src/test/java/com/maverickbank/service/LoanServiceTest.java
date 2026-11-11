package com.maverickbank.service;

import com.maverickbank.dto.request.LoanDto;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Loan;
import com.maverickbank.repository.LoanRepository;
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
public class LoanServiceTest {
    @InjectMocks
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;

    @Test
    public void addLoanTest() {
        LoanDto dto = new LoanDto("Personal Loan", new BigDecimal("1000"), new BigDecimal("100000"), 10.0, 12);

        loanService.addLoan(dto);

        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    public void addLoanTest_Invalid() {
        LoanDto dto = new LoanDto("Personal Loan", new BigDecimal("100000"), new BigDecimal("1000"), 10.0, 12);

        assertThrows(InvalidCallException.class, () -> loanService.addLoan(dto));
        verify(loanRepository, times(0)).save(any(Loan.class));
    }

    @Test
    public void getAllLoansTest() {
        when(loanRepository.findAll()).thenReturn(List.of(new Loan(), new Loan()));

        assertEquals(2, loanService.getAllLoans().size());
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    public void getLoanByIdTest() {
        Loan loan = new Loan();
        loan.setId(1L);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertEquals(1L, loanService.getLoanById(1L).getId());
        verify(loanRepository, times(1)).findById(1L);
    }

    @Test
    public void getLoanByIdTest_Invalid() {
        when(loanRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.getLoanById(5L));
        verify(loanRepository, times(1)).findById(5L);
    }
}
