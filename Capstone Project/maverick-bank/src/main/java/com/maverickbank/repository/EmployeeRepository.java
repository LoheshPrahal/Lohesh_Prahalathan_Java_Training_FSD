package com.maverickbank.repository;

import com.maverickbank.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select e from Employee e join e.user u where u.username = ?1")
    Employee findByUsername(String name);
}
