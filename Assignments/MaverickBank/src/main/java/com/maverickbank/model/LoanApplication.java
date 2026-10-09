package com.maverickbank.model;

import com.maverickbank.enums.LoanStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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

    public LoanApplication() {
    }

    public LoanApplication(int id, double requestedAmount, String purpose, LocalDate applicationDate, LoanStatus status, LocalDate reviewedDate, Loan loan, Account account, Employee reviewedBy) {
        this.id = id;
        this.requestedAmount = requestedAmount;
        this.purpose = purpose;
        this.applicationDate = applicationDate;
        this.status = status;
        this.reviewedDate = reviewedDate;
        this.loan = loan;
        this.account = account;
        this.reviewedBy = reviewedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public LocalDate getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(LocalDate reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Employee getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Employee reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "id=" + id +
                ", requestedAmount=" + requestedAmount +
                ", purpose='" + purpose + '\'' +
                ", applicationDate=" + applicationDate +
                ", status=" + status +
                ", reviewedDate=" + reviewedDate +
                ", loan=" + loan +
                ", account=" + account +
                ", reviewedBy=" + reviewedBy +
                '}';
    }
}
