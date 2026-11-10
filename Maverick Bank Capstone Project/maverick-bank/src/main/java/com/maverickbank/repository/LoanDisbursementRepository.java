package com.maverickbank.repository;

import com.maverickbank.model.LoanDisbursement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanDisbursementRepository extends JpaRepository<LoanDisbursement, Long> {
}
