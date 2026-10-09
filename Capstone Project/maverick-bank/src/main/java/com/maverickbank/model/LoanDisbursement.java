package com.maverickbank.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoanDisbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "disbursement_amount", nullable = false)
    private double disbursementAmount;

    @Column(name = "disbursed_date", nullable = false)
    private LocalDate disbursedDate;

    @ManyToOne
    @JoinColumn(name = "loan_applicaiton", nullable = false)
    private LoanApplication loanApplication;


}
