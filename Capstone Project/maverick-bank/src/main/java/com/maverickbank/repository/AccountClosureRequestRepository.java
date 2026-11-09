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
            select count(a) from AccountClosureRequest a
            where a.id = ?1 and a.status = ?2
        """)
    int countByAccountIdAndStatus(Long accountId, RequestStatus requestStatus);

    @Query("""
                select r.id, a.id, a.accountNumber, c.name, r.reason, r.requestDate, r.status
                from AccountClosureRequest r
                join r.account a
                join a.customer c
                where r.status = ?1
        """)
    List<AccountClosureRespDto> getByStatus(RequestStatus requestStatus);

    @Query("select r from AccountClosureRequest r where r.id = ?1")
    AccountClosureRequest getPendingRequest(Long requestId);
}
