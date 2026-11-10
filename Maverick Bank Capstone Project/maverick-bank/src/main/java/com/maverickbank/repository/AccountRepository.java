package com.maverickbank.repository;

import com.maverickbank.enums.AccountStatus;
import com.maverickbank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("""
                select a from Account a
                join a.customer c
                join c.user u
                where u.username = ?1
                order by a.id
        """)
    List<Account> getAccountsByCustomerUsername(String name);

    @Query("""
            select a from Account a
            where a.accountStatus = ?1
            order by a.id
            """)
    List<Account> getPendingAccounts(AccountStatus status);

    // account number is a String, so the parameter is a String too
    @Query("select count(a) from Account a where a.accountNumber = ?1")
    long countAccountNumber(String accountNumber);

    @Query("select a from Account a where a.accountNumber = ?1")
    Account getByAccountNumber(String accountNumber);

    @Query("select count(a) from Account a where a.accountStatus = ?1")
    Long countByAccountStatus(AccountStatus accountStatus);

    @Query("select sum(a.balance) from Account a where a.accountStatus = ?1")
    BigDecimal sumBalanceByAccountStatus(AccountStatus accountStatus);
}
