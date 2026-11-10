package com.maverickbank.repository;

import com.maverickbank.dto.response.AccountClosureRespDto;
import com.maverickbank.enums.RequestStatus;
import com.maverickbank.model.AccountClosureRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountClosureRequestRepository extends JpaRepository<AccountClosureRequest, Long> {
    @Query("""
            select count(r) from AccountClosureRequest r
            where r.account.id = ?1 and r.status = ?2
        """)
    long countByAccountIdAndStatus(Long accountId, RequestStatus requestStatus);

    @Query("""
                select r.id, a.id, a.accountNumber, c.name,
                r.reason, r.requestDate, r.status
                from AccountClosureRequest r
                join r.account a
                join a.customer c
                where r.status = ?1
                order by r.id
        """)
    List<AccountClosureRespDto> getByStatus(RequestStatus requestStatus);
}
