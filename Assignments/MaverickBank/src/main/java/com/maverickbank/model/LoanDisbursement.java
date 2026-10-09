package com.maverickbank.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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

    public LoanDisbursement() {
    }

    public LoanDisbursement(int id, double disbursementAmount, LocalDate disbursedDate, LoanApplication loanApplication) {
        this.id = id;
        this.disbursementAmount = disbursementAmount;
        this.disbursedDate = disbursedDate;
        this.loanApplication = loanApplication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public LocalDate getDisbursedDate() {
        return disbursedDate;
    }

    public void setDisbursedDate(LocalDate disbursedDate) {
        this.disbursedDate = disbursedDate;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    @Override
    public String toString() {
        return "LoanDisbursement{" +
                "id=" + id +
                ", disbursementAmount=" + disbursementAmount +
                ", disbursedDate=" + disbursedDate +
                ", loanApplication=" + loanApplication +
                '}';
    }
}
