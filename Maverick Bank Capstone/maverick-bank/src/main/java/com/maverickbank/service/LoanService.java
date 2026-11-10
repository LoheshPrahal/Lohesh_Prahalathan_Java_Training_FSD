package com.maverickbank.service;

import com.maverickbank.dto.request.LoanDto;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.mapper.LoanMapper;
import com.maverickbank.model.Loan;
import com.maverickbank.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class LoanService {
    private final LoanRepository loanRepository;

    public void addLoan(LoanDto dto) {
        if(dto.minAmount().compareTo(dto.maxAmount()) > 0){
            throw new InvalidCallException("Minimum amount cannot be more than maximum amount");
        }

        Loan loan = LoanMapper.convertDtoToEntity(dto);
        loanRepository.save(loan);

        // Log: a new loan is added
        log.info("Loan {} is added", dto.loanType());
    }

    public List<Loan> getAllLoans() {
        // Log: all the loans are listed
        log.info("Fetching all the loans");

        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan with id " + id + " not found"));
    }
}
