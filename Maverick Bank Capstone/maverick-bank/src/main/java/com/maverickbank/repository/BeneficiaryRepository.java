package com.maverickbank.repository;

import com.maverickbank.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    // count() returns a number, so the result is a long (not a boolean)
    @Query("select count(b) from Beneficiary b where b.customer.id = ?1 and b.accountNumber = ?2")
    long countSameBeneficiaryForCurrentCustomer(Long id, String s);

    @Query("select b from Beneficiary b join b.customer c join c.user u where u.username = ?1 order by b.id")
    List<Beneficiary> getByCustomerUsername(String name);
}
