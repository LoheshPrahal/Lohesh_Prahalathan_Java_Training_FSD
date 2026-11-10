package com.maverickbank.mapper;

import com.maverickbank.dto.request.CustomerDto;
import com.maverickbank.dto.response.CustomerRespDto;
import com.maverickbank.model.Customer;

import java.time.LocalDate;
import java.time.Period;

public class CustomerMapper {
    public static Customer convertDtoToEntity(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setName(customerDto.name());
        customer.setGender(customerDto.gender());
        customer.setEmail(customerDto.email());
        customer.setContact(customerDto.contact());
        customer.setAddress(customerDto.address());
        customer.setDateOfBirth(customerDto.dateOfBirth());
        customer.setAadhaarNumber(customerDto.aadhaarNumber());
        customer.setPanNumber(customerDto.panNumber());

        return customer;
    }

    public static CustomerRespDto convertEntityToDto(Customer customer){
        int age = Period.between(customer.getDateOfBirth(), LocalDate.now()).getYears();
        return new CustomerRespDto(
                customer.getId(),
                customer.getName(),
                customer.getGender(),
                customer.getEmail(),
                customer.getContact(),
                customer.getAddress(),
                customer.getDateOfBirth(),
                age,
                customer.getAadhaarNumber(),
                customer.getPanNumber(),
                customer.getStatus(),
                customer.getUser().getUsername()
        );
    }
}
