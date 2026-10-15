package com.maverickbank.mapper;

import com.maverickbank.dto.request.CustomerDto;
import com.maverickbank.model.Customer;

public class CustomerMapper {
    public static Customer convertDtoToEntity(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setName(customerDto.name());
        customer.setGender(customerDto.gender());
        customer.setEmail(customerDto.email());
        customer.setAddress(customerDto.address());
        customer.setDateOfBirth(customerDto.dateOfBirth());
        customer.setAadhaarNumber(customerDto.aadhaarNumber());

        return customer;
    }
}
