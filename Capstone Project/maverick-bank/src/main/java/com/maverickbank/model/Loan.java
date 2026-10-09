package com.maverickbank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "loan_type", nullable = false)
    private String loanType;

    @Column(name = "min_amount", nullable = false)
    private double minAmount;

    @Column(name = "max_amount", nullable = false)
    private double maxAmount;

    @Column(name = "interest_rate", nullable = false)
    private double interestRate;

    @Column(name = "tenure_months", nullable = false)
    private int tenureMonths;



}
