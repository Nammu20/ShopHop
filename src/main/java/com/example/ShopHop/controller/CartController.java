package com.example.ShopHop.controller;

import com.example.ShopHop.dto.RequestDto.ItemRequestDto;
import com.example.ShopHop.dto.ResponseDto.CartResponseDto;
import com.example.ShopHop.dto.ResponseDto.ItemResponseDto;
import com.example.ShopHop.model.Item;
import com.example.ShopHop.service.CartService;
import com.example.ShopHop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    ItemService itemService;

    @Autowired
    CartService cartService;

    @PostMapping("/add")
    public ResponseEntity addToCart(@RequestBody ItemRequestDto itemRequestDto) {
        try {
            Item item = itemService.createItem(itemRequestDto);
            CartResponseDto cartResponseDto = cartService.addToCart(item, itemRequestDto);
            return new ResponseEntity(cartResponseDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view-all-items-of-cart")
    public ResponseEntity viewItemsOfCart(@RequestParam("emailId") String customerEmailId){
        try {
            List<ItemResponseDto> itemResponseList= cartService.viewItemsOfCart(customerEmailId);
            return new ResponseEntity(itemResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-item-from-cart")
    public ResponseEntity removeItemFromCart(@RequestParam String customerEmailId, @RequestParam int itemId){
        try {
            String str= cartService.removeItemFromCart(customerEmailId, itemId);
            return new ResponseEntity(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
