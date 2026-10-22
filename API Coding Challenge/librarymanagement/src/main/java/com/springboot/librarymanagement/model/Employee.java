package com.springboot.librarymanagement.model;

import com.springboot.librarymanagement.enums.JobTitle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;
    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne()
    @JoinColumn(name = "company_id")
    private Company company;
}
