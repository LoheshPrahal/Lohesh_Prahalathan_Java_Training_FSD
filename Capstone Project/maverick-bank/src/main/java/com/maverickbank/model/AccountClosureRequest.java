package com.maverickbank.model;

import com.maverickbank.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private long id;

    @Column(name = "request_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate requestDate;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private Employee processedBy;


}
