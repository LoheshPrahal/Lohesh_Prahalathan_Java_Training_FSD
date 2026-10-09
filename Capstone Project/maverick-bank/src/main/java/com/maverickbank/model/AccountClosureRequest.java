package com.maverickbank.model;

import com.maverickbank.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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


}
