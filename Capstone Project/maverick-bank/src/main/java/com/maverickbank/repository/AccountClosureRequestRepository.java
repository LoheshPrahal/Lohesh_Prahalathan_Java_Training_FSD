package com.maverickbank.repository;

import com.maverickbank.model.AccountClosureRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountClosureRequestRepository extends JpaRepository<AccountClosureRequest, Long> {
}
