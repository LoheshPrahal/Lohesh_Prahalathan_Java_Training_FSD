package com.springboot.librarymanagement.model;

import com.springboot.librarymanagement.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
}
