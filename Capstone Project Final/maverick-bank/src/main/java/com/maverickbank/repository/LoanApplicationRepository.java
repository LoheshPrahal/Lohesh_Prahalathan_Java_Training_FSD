package com.maverickbank.repository;

import com.maverickbank.dto.response.LoanApplicationRespDto;
import com.maverickbank.dto.response.LoanStatusCountDto;
import com.maverickbank.enums.LoanStatus;
import com.maverickbank.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    @Query("""
        select la.id, l.loanType, a.id, a.accountNumber,
        c.name, la.requestedAmount, l.interestRate, l.tenureMonths, la.purpose, la.applicationDate, la.status,
        la.reviewedDate, la.remainingAmount
        from LoanApplication la
        join la.loan l
        join la.account a
        join a.customer c
        join c.user u
        where u.username = ?1
        order by la.id desc
    """)
    List<LoanApplicationRespDto> getByCustomerUsername(String customerUsername);

    @Query("""
        select la.id, l.loanType, a.id, a.accountNumber,
        c.name, la.requestedAmount, l.interestRate, l.tenureMonths, la.purpose, la.applicationDate, la.status,
        la.reviewedDate, la.remainingAmount
        from LoanApplication la
        join la.loan l
        join la.account a
        join a.customer c
        join c.user u
        where u.username = ?1 and la.status in (?2)
        order by la.id desc
    """)
    // Repayment - more than one status can be asked (disbursed and closed loans of the customer)
    List<LoanApplicationRespDto> getByCustomerUsernameAndStatusIn(String customerUsername, List<LoanStatus> loanStatuses);

    @Query("""
        select la.id, l.loanType, a.id, a.accountNumber,
        c.name, la.requestedAmount, l.interestRate, l.tenureMonths, la.purpose, la.applicationDate, la.status,
        la.reviewedDate, la.remainingAmount
        from LoanApplication la
        join la.loan l
        join la.account a
        join a.customer c
        where la.status = ?1
        order by la.id
    """)
    List<LoanApplicationRespDto> getByStatus(LoanStatus status);

    // Repayment - loans of the given status which have nothing more to pay (the remaining amount is zero)
    @Query("""
        select la.id, l.loanType, a.id, a.accountNumber,
        c.name, la.requestedAmount, l.interestRate, l.tenureMonths, la.purpose, la.applicationDate, la.status,
        la.reviewedDate, la.remainingAmount
        from LoanApplication la
        join la.loan l
        join la.account a
        join a.customer c
        where la.status = ?1 and la.remainingAmount = 0
        order by la.id
    """)
    List<LoanApplicationRespDto> getFullyPaidLoans(LoanStatus status);

    @Query("""
            select la.status, count(la)
            from LoanApplication la
            group by la.status
            """)
    List<LoanStatusCountDto> getLoanApplicationNumbersByStatus();
}
