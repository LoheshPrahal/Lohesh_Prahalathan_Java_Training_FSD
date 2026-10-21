package com.maverickbank.mapper;

import com.maverickbank.dto.request.CustomerDto;
import com.maverickbank.dto.response.CustomerRespDto;
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

    public static CustomerRespDto convertEntityToDto(Customer customer){
        return new CustomerRespDto(
                customer.getId(),
                customer.getName(),
                customer.getGender(),
                customer.getEmail(),
                customer.getContact(),
                customer.getAddress(),
                customer.getDateOfBirth(),
                customer.getAadhaarNumber(),
                customer.getPanNumber(),
                customer.getStatus(),
                customer.getUser().getUsername()
        );
    }
}
