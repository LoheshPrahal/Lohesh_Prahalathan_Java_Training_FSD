package com.maverickbank.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
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
    private long id;

    @Column(name = "disbursement_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal disbursementAmount;

    @Column(name = "disbursed_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate disbursedDate;

    @OneToOne
    @JoinColumn(name = "loan_application_id", nullable = false, unique = true)
    private LoanApplication loanApplication;


}
