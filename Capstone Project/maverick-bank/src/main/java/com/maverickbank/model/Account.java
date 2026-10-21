package com.maverickbank.model;

import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private double balance = 0;

    @Column(name = "account_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "opened_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant openedDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee approvedBy;


}
