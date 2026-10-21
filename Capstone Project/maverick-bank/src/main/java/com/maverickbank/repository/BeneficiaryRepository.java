package com.maverickbank.repository;

import com.maverickbank.model.Beneficiary;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    @Query("select count(b) from Beneficiary b where b.customer.id = ?1 and b.accountNumber = ?2")
    boolean countSameBeneficiaryForCurrentCustomer(long id, String s);

    @Query("select b from Beneficiary b join b.customer c join c.user u where u.username = ?1")
    List<Beneficiary> getByCustomerUsername(String name);
}
