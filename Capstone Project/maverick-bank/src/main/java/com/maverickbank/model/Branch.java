package com.maverickbank.model;

import com.maverickbank.enums.BranchName;
import com.maverickbank.enums.IfscCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "branch_name", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BranchName branchName;

    @Column(name = "ifsc_code", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private IfscCode ifscCode;


}
