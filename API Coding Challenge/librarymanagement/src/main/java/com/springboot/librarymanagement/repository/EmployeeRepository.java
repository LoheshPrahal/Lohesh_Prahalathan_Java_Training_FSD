package com.springboot.librarymanagement.repository;

import com.springboot.librarymanagement.dto.response.EmployeeRespDto;
import com.springboot.librarymanagement.enums.JobTitle;
import com.springboot.librarymanagement.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            select e.name, e.jobTitle, c.name, c.status
            from Employee e
            join e.company c
            where e.jobTitle = ?1 and c.name = ?2
        """)
    public List<EmployeeRespDto> getEmployeesByJobTitleAndCompanyName(JobTitle jobTitle, String companyName, Pageable pageable);
}
