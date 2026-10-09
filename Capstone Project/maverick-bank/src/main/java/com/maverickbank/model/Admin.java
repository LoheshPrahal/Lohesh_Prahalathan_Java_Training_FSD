package com.maverickbank.model;


import com.maverickbank.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
