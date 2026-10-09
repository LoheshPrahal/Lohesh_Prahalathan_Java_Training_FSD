package com.maverickbank.model;

import com.maverickbank.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "requested_amount", nullable = false)
    private double requestedAmount;

    @Column(nullable = false)
    private String purpose;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private LoanStatus status;

    @Column(name = "reviewed_date", nullable = false)
    private LocalDate reviewedDate;



    @OneToOne
    @JoinColumn(name = "loan_id", nullable = false, unique = true)
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "reviewed_by", nullable = false)
    private Employee reviewedBy;


}
