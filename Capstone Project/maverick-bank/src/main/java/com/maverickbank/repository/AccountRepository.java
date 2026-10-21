package com.maverickbank.repository;

import com.maverickbank.dto.response.AccountRespDto;
import com.maverickbank.enums.AccountStatus;
import com.maverickbank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("""
                select a from Account a
                join a.customer c
                join c.user u
                where u.username = ?1
        """)
    List<Account> getAccountsByCustomerUsername(String name);

    @Query("""
            select a from Account a
            where a.accountStatus = ?1
            """)
    List<Account> getPendingAccounts(AccountStatus status);

    @Query("select count(a) from Account a where a.accountNumber = ?1")
    int countAccountNumber(long accountNumber);

    @Query("select a from Account a where a.accountNumber = ?1")
    Account getByAccountNumber(String accountNumber);
}
