package com.maverickbank.repository;

import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.AccountType;
import com.maverickbank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Filter - accounts of a customer, the account type and the account status are optional
    @Query("""
                select a from Account a
                join a.customer c
                join c.user u
                where u.username = ?1
                and (?2 is null or a.accountType = ?2)
                and (?3 is null or a.accountStatus = ?3)
                order by a.id
        """)
    List<Account> getMyAccountsDynamically(String name, AccountType accountType, AccountStatus accountStatus);

    // Filter - accounts waiting for approval, the account type and the branch are optional
    @Query("""
            select a from Account a
            join a.branch b
            where a.accountStatus = ?1
            and (?2 is null or a.accountType = ?2)
            and (?3 is null or b.id = ?3)
            order by a.id
            """)
    List<Account> getPendingAccountsDynamically(AccountStatus status, AccountType accountType, Long branchId);

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
