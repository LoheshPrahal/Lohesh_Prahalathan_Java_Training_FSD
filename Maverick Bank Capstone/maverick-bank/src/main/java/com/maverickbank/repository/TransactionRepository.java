package com.maverickbank.repository;

import com.maverickbank.dto.response.TransactionRespDto;
import com.maverickbank.enums.TransactionType;
import com.maverickbank.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
            select t.id, a.accountNumber, t.transactionType, t.amount, t.transactionDate, t.description, t.transactionStatus
            from Transaction t
            join t.account a
            where a.accountNumber = ?1 
            and (?2 is null or t.transactionDate >= ?2)
            and (?3 is null or t.transactionDate < ?3)
            and (?4 is null or t.transactionType = ?4)
            order by t.id desc
            """)
    List<TransactionRespDto> getMyTransactionsDynamically(String accountNumber, Instant fromDate, Instant toDate, TransactionType transactionType, Pageable pageable);

    @Query("""
            select t.id, a.accountNumber, t.transactionType, t.amount, t.transactionDate, t.description, t.transactionStatus
            from Transaction t
            join t.account a
            where (?1 is null or t.transactionDate >= ?1)
            and (?2 is null or t.transactionDate < ?2)
            and (?3 is null or t.transactionType = ?3)
            order by t.id desc
            """)
    List<TransactionRespDto> getAllTransactions(Instant fromDate, Instant toDate, TransactionType transactionType, Pageable pageable);

    @Query("""
            select t.id, a.accountNumber, t.transactionType, t.amount, t.transactionDate, t.description, t.transactionStatus
            from Transaction t
            join t.account a
            where t.amount >= ?1
            order by t.id desc
            """)
    List<TransactionRespDto> getHighValueTransactions(BigDecimal threshold, Pageable pageable);

    @Query("select sum(t.amount) from Transaction t where t.account.id = ?1 and t.transactionType in (?2)")
    BigDecimal sumAmountByAccountIdAndTypes(Long accountId, List<TransactionType> list);

    @Query("select sum(t.amount) from Transaction t where t.transactionType in (?1)")
    BigDecimal sumAmountByTypes(List<TransactionType> types);
}
