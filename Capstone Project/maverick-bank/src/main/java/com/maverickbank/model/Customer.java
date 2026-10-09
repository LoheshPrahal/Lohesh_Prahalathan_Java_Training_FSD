package com.maverickbank.model;

import com.maverickbank.enums.CustomerGender;
import com.maverickbank.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CustomerGender gender;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "aadhaar_number", nullable = false)
    private String aadhaarNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
