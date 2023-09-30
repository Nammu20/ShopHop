package com.example.ShopHop.controller;

import com.example.ShopHop.dto.RequestDto.CustomerRequestDto;
import com.example.ShopHop.dto.RequestDto.UpdateInfoUsingMobNo;
import com.example.ShopHop.dto.ResponseDto.CustomerResponseDto;
import com.example.ShopHop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    //api  to add customer
    @PostMapping("/add")
    public ResponseEntity addCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        try {

            CustomerResponseDto customerResponseDto = customerService.addCustomer(customerRequestDto);
            return new ResponseEntity(customerResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //api to get customer using email
    @GetMapping("/get-customer-using-email/{emailId}")
    public ResponseEntity getCustomerUsingEmail(@PathVariable("emailId") String emailId){
        try {
            CustomerResponseDto customerResponseDto=customerService.getCustomerUsingEmail(emailId);
            return new ResponseEntity(customerResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //api to update customer using mob no
    @PutMapping("/update-using-mobNo")
    public ResponseEntity updateInfoByMobNo(@RequestBody UpdateInfoUsingMobNo updateInfoUsingMobNo){
        try {
            CustomerResponseDto customerResponseDto= customerService.updateInfoByMobNo(updateInfoUsingMobNo);
            return new ResponseEntity(customerResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //api to delete customer
    @DeleteMapping("/delete-either-by-email-or-by-mobNo")
    public ResponseEntity deleteCustomerEitherBYEmailIdOrByMobNo(@RequestParam("emailId") String emailId,
                                                                 @RequestParam("mobNo") String mobNo) {
        try {
            String str = customerService.deleteCustomerEitherBYEmailIdOrByMobNo(emailId, mobNo);
            return new ResponseEntity<>(str, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login-customer")
    public ResponseEntity loginCustomer(@RequestParam String emailId, @RequestParam String password){
        try{
            String str = customerService.loginCustomer(emailId,password);
            return new ResponseEntity<>(str,HttpStatus.OK);
        }
        catch (Exception e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/change-password")
    public ResponseEntity changePassword(@RequestParam String mobNo, @RequestParam String oldPassword, @RequestParam String newPassword){
        try{
            String str = customerService.changePassword(mobNo,oldPassword,newPassword);
            return new ResponseEntity<>(str, HttpStatus.OK);
        }
        catch (Exception e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

