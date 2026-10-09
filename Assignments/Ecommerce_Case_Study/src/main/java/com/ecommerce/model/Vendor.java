package com.ecommerce.model;

import com.ecommerce.enums.VendorName;

public class Vendor {
    private int id;
    private VendorName name;
    private String email;

    public Vendor() {
    }

    public Vendor(int id, VendorName name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VendorName getName() {
        return name;
    }

    public void setName(VendorName name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "id=" + id +
                ", name=" + name +
                ", email='" + email + '\'' +
                '}';
    }
}
