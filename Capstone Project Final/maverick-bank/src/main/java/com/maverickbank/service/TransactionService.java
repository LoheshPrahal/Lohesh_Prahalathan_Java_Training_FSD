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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class TransactionService {
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    // Types which increase and decrease the balance of an account (used by the summary and the statement)
    private static final List<TransactionType> CREDIT_TYPES = List.of(TransactionType.DEPOSIT, TransactionType.LOAN_DISBURSEMENT, TransactionType.TRANSFER_IN);
    // Repayment - LOAN_REPAYMENT is money going out of the account
    private static final List<TransactionType> DEBIT_TYPES = List.of(TransactionType.TRANSFER_OUT, TransactionType.WITHDRAWAL, TransactionType.LOAN_REPAYMENT);
    private static final DateTimeFormatter STATEMENT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @Transactional(rollbackFor = Exception.class)
    public void deposit(String name, @Valid TransactionDto dto) {
        Account account = getActiveOwnedAccount(dto.accountId(), name);

        accountService.credit(account, dto.amount());
        recordTransaction(account, TransactionType.DEPOSIT, dto.amount(), getDescription(dto.description(), "Cash Deposit"), null);

        // Log: money is deposited by the customer
        log.info("Deposit of {} done by customer {} on account id {}", dto.amount(), name, dto.accountId());
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

    @Transactional(rollbackFor = Exception.class)
    public void withdraw(String name, @Valid TransactionDto dto) {
        Account account = getActiveOwnedAccount(dto.accountId(), name);

        accountService.debit(account,dto.amount());
        recordTransaction(account, TransactionType.WITHDRAWAL, dto.amount(), getDescription(dto.description(), "Cash Withdrawal"), null);

        // Log: money is withdrawn by the customer
        log.info("Withdrawal of {} done by customer {} on account id {}", dto.amount(), name, dto.accountId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void transfer(String name, @Valid TransferDto dto) {
        Account source = getActiveOwnedAccount(dto.accountId(), name);

        Beneficiary beneficiary = beneficiaryRepository.findById(dto.beneficiaryId()).orElseThrow(() ->
                new ResourceNotFoundException("Beneficiary not found!"));

        if(beneficiary.getCustomer().getId() != source.getCustomer().getId())
            throw new OwnershipException("This beneficiary does not belong to the logged-in user");

        Account destination = accountService.getByAccountNumber(beneficiary.getAccountNumber());

        if(destination.getAccountStatus() != AccountStatus.ACTIVE)
            throw new InvalidCallException("Beneficiary account is not active");

        if(destination.getId() == source.getId())
            throw new InvalidCallException("Cannot transfer to the same account");

        accountService.debit(source, dto.amount());
        accountService.credit(destination, dto.amount());

        recordTransaction(source, TransactionType.TRANSFER_OUT, dto.amount(), getDescription(dto.description(), "Transfer to " + beneficiary.getAccountName()), beneficiary);
        recordTransaction(destination, TransactionType.TRANSFER_IN, dto.amount(), getDescription(dto.description(), "Transfer from " + source.getAccountNumber()), null);

        // Log: money is transferred to a beneficiary
        log.info("Transfer of {} done by customer {} from account id {} to beneficiary id {}", dto.amount(), name, dto.accountId(), dto.beneficiaryId());
    }

    public List<TransactionRespDto> getTransactionsByAccountDynamicFilter(String username, String accountNumber, LocalDate fromDate, LocalDate toDate, TransactionType transactionType, Integer page, Integer size) {
        // Log: customer lists the transactions of an own account
        log.info("Fetching transactions of account {} for customer {}", accountNumber, username);

        Account account = accountService.getByAccountNumber(accountNumber);

        if(!account.getCustomer().getUser().getUsername().equals(username))
            throw new OwnershipException("This account does not belong to the currently logged-in user");
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.getMyTransactionsDynamically(accountNumber, getStart(fromDate), getEnd(toDate), transactionType, pageable);
    }


    public List<TransactionRespDto> getTransactionsByAccountDynamically(String accountNumber, LocalDate fromDate, LocalDate toDate, TransactionType transactionType, Integer page, Integer size) {
        // Log: employee lists the transactions of an account
        log.info("Fetching transactions of account {}", accountNumber);

        Account account = accountService.getByAccountNumber(accountNumber);

        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.getMyTransactionsDynamically(accountNumber, getStart(fromDate), getEnd(toDate), transactionType, pageable);
    }

    public List<TransactionRespDto> getAllTransactions(LocalDate fromDate, LocalDate toDate, TransactionType transactionType, Integer page, Integer size) {
        // Log: employee lists the transactions of all the accounts
        log.info("Fetching transactions of all the accounts");

        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.getAllTransactions(getStart(fromDate), getEnd(toDate), transactionType, pageable);
    }

    public AccountSummaryDto getAccountSummary(Long accountId) {
        // Log: total inbound and outbound of an account
        log.info("Fetching summary of account id {}", accountId);

        Account account = accountService.getById(accountId);

        BigDecimal totalCredit = transactionRepository.sumAmountByAccountIdAndTypes(accountId, CREDIT_TYPES);
        BigDecimal totalDebit = transactionRepository.sumAmountByAccountIdAndTypes(accountId, DEBIT_TYPES);

        return new AccountSummaryDto(account.getId(), account.getAccountNumber(),
                totalCredit == null ? BigDecimal.ZERO : totalCredit,
                totalDebit == null ? BigDecimal.ZERO : totalDebit);
    }



    // Statement - the customer downloads the transactions of an own account as csv (the dates are optional)
    public String getStatementCsv(String username, String accountNumber, LocalDate fromDate, LocalDate toDate) {
        // Log: customer generates the account statement
        log.info("Generating statement of account {} for customer {}", accountNumber, username);

        Account account = accountService.getByAccountNumber(accountNumber);

        if(!account.getCustomer().getUser().getUsername().equals(username))
            throw new OwnershipException("This account does not belong to the currently logged-in user");

        // All the transactions of the account, the oldest first (the balance is calculated from the first transaction)
        List<TransactionRespDto> list = new ArrayList<>(transactionRepository.getMyTransactionsDynamically(accountNumber, null, null, null, Pageable.unpaged()));
        Collections.reverse(list);

        Instant start = getStart(fromDate);
        Instant end = getEnd(toDate);

        StringBuilder csv = new StringBuilder("Date,Description,Type,Credit,Debit,Balance\n");
        BigDecimal balance = BigDecimal.ZERO;

        for(TransactionRespDto t : list){
            boolean isCredit = CREDIT_TYPES.contains(t.transactionType());
            balance = isCredit ? balance.add(t.amount()) : balance.subtract(t.amount());

            // Only the selected dates are in the statement, but the balance counts the earlier transactions also
            if((start != null && t.transactionDate().isBefore(start)) || (end != null && !t.transactionDate().isBefore(end))) continue;

            csv.append(STATEMENT_DATE.format(t.transactionDate())).append(',')
                    .append(csvValue(t.description())).append(',')
                    .append(t.transactionType()).append(',')
                    .append(isCredit ? t.amount().toPlainString() : "").append(',')
                    .append(isCredit ? "" : t.amount().toPlainString()).append(',')
                    .append(balance.toPlainString()).append('\n');
        }

        return csv.toString();
    }

    // Text is put in quotes (a quote inside is doubled), and a leading = + - @ is made harmless for spreadsheets
    private String csvValue(String value) {
        if(value == null) return "\"\"";
        if(value.startsWith("=") || value.startsWith("+") || value.startsWith("-") || value.startsWith("@")) value = "'" + value;
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private Instant getStart(LocalDate fromDate) {
        return fromDate == null ? null : fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private Instant getEnd(LocalDate toDate) {
        return toDate == null ? null : toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
    }


}
