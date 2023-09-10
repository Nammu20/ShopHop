package com.example.ShopHop.controller;

import com.example.ShopHop.dto.RequestDto.CardRequestDto;
import com.example.ShopHop.dto.ResponseDto.CardResponseDto;
import com.example.ShopHop.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    CardService cardService;

    //api to add card
    @PostMapping("/add")
    public ResponseEntity addCard(@RequestBody CardRequestDto cardRequestDto) {
        try {
            CardResponseDto cardResponseDto = cardService.addCard(cardRequestDto);
            return new ResponseEntity(cardResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //api to get cards with card type & min expiry date
    @GetMapping("/get-cards-details-by-emailId")
    public ResponseEntity cardDetailWithEmail(@RequestParam("emailId") String emailId){
        try {
            List<CardResponseDto> cardResponseList= cardService.cardDetailWithEmail(emailId);
            return new ResponseEntity(cardResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //api to delete a card of customer using email & card no
    @DeleteMapping("/delete")
    public ResponseEntity removeCard(@RequestParam("customerEmailId") String emailId, @RequestParam("cardNo") String cardNo){
        try {
            String str= cardService.removeCard(emailId, cardNo);
            return new ResponseEntity(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}