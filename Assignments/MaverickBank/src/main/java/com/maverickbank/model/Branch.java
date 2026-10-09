package com.maverickbank.model;

import com.maverickbank.enums.BranchName;
import com.maverickbank.enums.IfscCode;
import jakarta.persistence.*;

@Entity
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

    public Branch() {
    }

    public Branch(int id, String name, String address, BranchName branchName, IfscCode ifscCode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.branchName = branchName;
        this.ifscCode = ifscCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BranchName getBranchName() {
        return branchName;
    }

    public void setBranchName(BranchName branchName) {
        this.branchName = branchName;
    }

    public IfscCode getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(IfscCode ifscCode) {
        this.ifscCode = ifscCode;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", branchName=" + branchName +
                ", ifscCode=" + ifscCode +
                '}';
    }
}
