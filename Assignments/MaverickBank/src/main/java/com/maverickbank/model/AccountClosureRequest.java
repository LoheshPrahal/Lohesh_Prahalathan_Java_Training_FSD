package com.maverickbank.model;

import com.maverickbank.enums.AccountStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class AccountClosureRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(nullable = false)
    private String reason;

    @Column(name = "account_status", nullable = false)
    private AccountStatus status;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "processed_by", nullable = false)
    private Employee processedBy;

    public AccountClosureRequest() {
    }

    public AccountClosureRequest(int id, LocalDate requestDate, String reason, AccountStatus status, Account account, Employee processedBy) {
        this.id = id;
        this.requestDate = requestDate;
        this.reason = reason;
        this.status = status;
        this.account = account;
        this.processedBy = processedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Employee getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Employee processedBy) {
        this.processedBy = processedBy;
    }

    @Override
    public String toString() {
        return "AccountClosureRequest{" +
                "id=" + id +
                ", requestDate=" + requestDate +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", account=" + account +
                ", processedBy=" + processedBy +
                '}';
    }
}
