package com.example.ShopHop.transformer;

import com.example.ShopHop.dto.RequestDto.CustomerRequestDto;
import com.example.ShopHop.dto.ResponseDto.CustomerResponseDto;
import com.example.ShopHop.model.Customer;

public class CustomerTransformer {
    public static Customer CustomerRequestDtoToCustomer(CustomerRequestDto customerRequestDto){

        return Customer.builder()
                .name(customerRequestDto.getName())
                .dob(customerRequestDto.getDob())
                .mobNo(customerRequestDto.getMobNo())
                .address(customerRequestDto.getAddress())
                .emailId(customerRequestDto.getEmailId())
                .password(customerRequestDto.getPassword())
              //  .gender(customerRequestDto.getGender())
                .build();
    }

    public static CustomerResponseDto CustomerToCustomerResponseDto(Customer customer){

        return CustomerResponseDto.builder()
                .name(customer.getName())
                .dob(customer.getDob())
                .mobNo(customer.getMobNo())
                .address(customer.getAddress())
                .emailId(customer.getEmailId())
               // .gender(customer.getGender())
                .build();
    }

}
