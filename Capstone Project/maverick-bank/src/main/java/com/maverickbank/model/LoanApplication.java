package com.maverickbank.model;

import com.maverickbank.enums.LoanStatus;
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
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "requested_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    @Column(nullable = false)
    private String purpose;

    @Column(name = "application_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate applicationDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private LoanStatus status;

    @Column(name = "reviewed_date")
    private LocalDate reviewedDate;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private Employee reviewedBy;


}
