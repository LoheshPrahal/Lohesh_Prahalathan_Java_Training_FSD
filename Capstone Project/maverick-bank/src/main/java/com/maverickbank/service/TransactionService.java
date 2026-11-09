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
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.Account;
import com.maverickbank.model.Beneficiary;
import com.maverickbank.model.Transaction;
import com.maverickbank.repository.BeneficiaryRepository;
import com.maverickbank.repository.TransactionRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public void deposit(String name, @Valid TransactionDto dto) {
        Account account = getActiveOwnedAccount(dto.accountId(), name);

        accountService.credit(account, dto.amount());

        recordTransaction(account, TransactionType.DEPOSIT, dto.amount(), getDescription(dto.description(), "Cash Deposit"), null);

    }

    private Account getActiveOwnedAccount(Long accountId, String name) {
        Account account = accountService.getOwnedAccount(accountId, name);

        if(account.getAccountStatus() != AccountStatus.ACTIVE){
            throw new InvalidCallException("Account is not active");
        }

        return account;
    }

    public Transaction recordTransaction(Account account, TransactionType transactionType, BigDecimal amount, String description, Beneficiary beneficiary) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setBeneficiary(beneficiary);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction);
    }


    public String getDescription(String description, String defaultDescription){
        if(description == null || description.isBlank()) return defaultDescription;
        return description;
    }

    public void withdraw(String name, @Valid TransactionDto dto) {
        Account account = getActiveOwnedAccount(dto.accountId(), name);
        accountService.debit(account,dto.amount());

        recordTransaction(account, TransactionType.WITHDRAWAL, dto.amount(), getDescription(dto.description(), "Cash Withdrawal"), null);
    }

    public void transfer(String name, @Valid TransferDto dto) {
        Account source = getActiveOwnedAccount(dto.accountId(), name);
        Beneficiary beneficiary = beneficiaryRepository.findById(dto.beneficiaryId()).orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found!"));

        if(beneficiary.getCustomer().getId() != source.getCustomer().getId()) throw new OwnershipException("This beneficiary does not belong to the logged-in user");
        Account destination = accountService.getByAccountNumber(beneficiary.getAccountNumber());

        if(destination.getAccountStatus() != AccountStatus.ACTIVE) throw new InvalidCallException("Beneficiary account is not active");

        if(destination.getId() == source.getId()) throw new InvalidCallException("Cannot transfer to the same account");

        accountService.debit(source, dto.amount());
        accountService.credit(destination, dto.amount());

        recordTransaction(source, TransactionType.TRANSFER_OUT, dto.amount(), getDescription(dto.description(), "Transfer to " + beneficiary.getAccountName()), beneficiary);
        recordTransaction(destination, TransactionType.TRANSFER_IN, dto.amount(), getDescription(dto.description(), "Transfer from " + source.getAccountNumber()), null);
    }

    public List<TransactionRespDto> getTransactionsByAccountDynamically(String accountNumber, LocalDate fromDate, LocalDate toDate, TransactionType transactionType, Integer page, Integer size) {
        accountService.getByAccountNumber(accountNumber);
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.getMyTransactionsDynamically(accountNumber, fromDate == null ? null : fromDate.atStartOfDay(ZoneOffset.UTC).toInstant(), toDate == null ? null : toDate.atTime(11, 59).toInstant(ZoneOffset.UTC), transactionType, pageable);
    }

    public List<TransactionRespDto> getAllTransactions(LocalDate fromDate, LocalDate toDate, TransactionType transactionType, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.getAllTransactions(fromDate == null ? null : fromDate.atStartOfDay(ZoneOffset.UTC).toInstant(), toDate == null ? null : toDate.atTime(11, 59).toInstant(ZoneOffset.UTC), transactionType, pageable);
    }

    public AccountSummaryDto getAccountSummary(Long accountId) {
        Account account = accountService.getById(accountId);
        List<TransactionType> creditList = List.of(TransactionType.DEPOSIT, TransactionType.LOAN_DISBURSEMENT, TransactionType.TRANSFER_IN);
        List<TransactionType> debitList = List.of(TransactionType.TRANSFER_OUT, TransactionType.WITHDRAWAL);
        BigDecimal totalCredit = transactionRepository.sumAmountByAccountIdAndTypes(accountId, creditList);
        BigDecimal totalDebit = transactionRepository.sumAmountByAccountIdAndTypes(accountId, debitList);
        return new AccountSummaryDto(account.getId(), account.getAccountNumber(), totalCredit, totalDebit);
    }
}
