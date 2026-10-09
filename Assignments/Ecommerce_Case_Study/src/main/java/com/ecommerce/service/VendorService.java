package com.ecommerce.service;

import com.ecommerce.model.Vendor;
import com.ecommerce.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public Vendor getVendor(String s) {
        return vendorRepository.getVendor(s);
    }


    public Map<String, Integer> countProductsByVendor() {
        return vendorRepository.countProductsByVendor();
    }
}
