package com.example.ShopHop.controller;

import com.example.ShopHop.dto.RequestDto.DirectOrderRequestDto;
import com.example.ShopHop.dto.RequestDto.OrderListRequestDto;
import com.example.ShopHop.dto.ResponseDto.OrderedResponseDto;
import com.example.ShopHop.service.OrderedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordered")


public class OrderedController {

    @Autowired
    OrderedService orderedService;

   @PostMapping("/place-order-directly")
   public ResponseEntity placedDirectOrder(@RequestBody OrderListRequestDto orderListRequestDto) {
        try {
            List<OrderedResponseDto> orderedResponseDto = orderedService.directPlaceOrder(orderListRequestDto);
            return new ResponseEntity(orderedResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/cancel-order")
    public ResponseEntity cancelOrder(@RequestParam("email") String customerEmailId,
                                      @RequestParam("orderNo") String orderNo){
        try {
            String str= orderedService.cancelOrder(customerEmailId, orderNo);
            return new ResponseEntity<>(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
   }
    @GetMapping("/get-all-orders-of-customer")
    public ResponseEntity getAllOrdersOfCustomer(@RequestParam("emailId") String emailId){
        try {
            List<OrderedResponseDto> orderResponseList= orderedService.getAllOrdersOfCustomer(emailId);
            return new ResponseEntity(orderResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}

