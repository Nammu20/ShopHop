package com.example.ShopHop.controller;

import com.example.ShopHop.dto.RequestDto.DirectOrderRequestDto;
import com.example.ShopHop.dto.ResponseDto.OrderedResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")

public class OrderedController {
   // @PostMapping("/place-order-directly")
   /* public ResponseEntity placedDirectOrder(@RequestBody DirectOrderRequestDto directOrderRequestDto) {
        try {
            OrderedResponseDto orderedResponseDto = orderedService.directPlaceOrder(directOrderRequestDto);
            return new ResponseEntity(orderedResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }*/
}

