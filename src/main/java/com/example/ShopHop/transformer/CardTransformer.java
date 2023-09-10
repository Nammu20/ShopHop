package com.example.ShopHop.transformer;

import com.example.ShopHop.dto.RequestDto.CardRequestDto;
import com.example.ShopHop.dto.ResponseDto.CardResponseDto;
import com.example.ShopHop.model.Card;

public class CardTransformer {
    public static Card CardRequestToCard(CardRequestDto cardRequestDto){

        return Card.builder()
                .cardNo(cardRequestDto.getCardNo())
                .cvv(cardRequestDto.getCvv())
                .expiryDate(cardRequestDto.getExpiryDate())
                .build();
    }

    public static CardResponseDto CardToCardResponse(Card card){
        return CardResponseDto.builder()
                .customerName(card.getCustomer().getName())
                .cardNo(maskedCard(card.getCardNo()))
                .expiryDate(card.getExpiryDate())
                .cardType(card.getCardType())
                .cvv(card.getCvv())
                .emailId(card.getCustomer().getEmailId())
                .build();
    }
    public static String maskedCard(String cardNo){
        // Only showing last 4 digits to Customer
        StringBuilder cardUsed= new StringBuilder();
        for(int i=0; i<cardNo.length(); i++){
            if(i<cardNo.length()-4){
                cardUsed.append("x");
            }
            else cardUsed.append(cardNo.charAt(i));
        }

        return cardUsed.toString();
    }
}
