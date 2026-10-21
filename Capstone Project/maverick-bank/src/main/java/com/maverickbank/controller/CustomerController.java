package com.maverickbank.controller;

import com.maverickbank.dto.request.CustomerDto;
import com.maverickbank.dto.request.CustomerUpdateDto;
import com.maverickbank.dto.response.CustomerRespDto;
import com.maverickbank.model.Customer;
import com.maverickbank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/api/customer/add")
    public void insertCustomer(@Valid @RequestBody CustomerDto customerDto){
        customerService.insertCustomer(customerDto);
    }

    @GetMapping("/api/customer/profile")
    public CustomerRespDto getProfile(Principal principal){
        return customerService.getProfile(principal.getName());
    }

    @GetMapping("/api/customer/{id}")
    public Customer getCustomerById(@PathVariable long id){
        return customerService.getCustomerById(id);
    }

    @GetMapping("/api/customer/all")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PutMapping("/api/customer/{id}")
    public void update(@PathVariable long id, @RequestBody CustomerUpdateDto dto){
        customerService.update(id, dto);
    }

    @PutMapping("/api/customer/deactivate/{id}")
    public void deactivate(@PathVariable long id){
        customerService.deactivate(id);
    }
}
