package com.maverickbank.model;

import jakarta.persistence.*;

@Entity
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


    public Loan() {
    }

    public Loan(int id, String loanType, double minAmount, double maxAmount, double interestRate, int tenureMonths) {
        this.id = id;
        this.loanType = loanType;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", loanType='" + loanType + '\'' +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                ", interestRate=" + interestRate +
                ", tenureMonths=" + tenureMonths +
                '}';
    }
}
