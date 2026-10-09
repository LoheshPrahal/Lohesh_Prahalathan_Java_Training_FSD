package com.maverickbank.model;

import com.maverickbank.enums.AccountStatus;
import com.maverickbank.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private double balance;

    @Column(name = "account_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "opened_date", nullable = false)
    private LocalDate openedDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee approvedBy;


}
