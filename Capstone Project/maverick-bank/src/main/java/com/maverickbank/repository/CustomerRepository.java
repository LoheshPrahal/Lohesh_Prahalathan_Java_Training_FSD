package com.maverickbank.repository;

import com.maverickbank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("""
        select c from Customer c 
        join c.user u
        where u.username = ?1
        """)
    Customer findByUsername(String customerUsername);
}
