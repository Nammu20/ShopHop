package com.example.ShopHop.transformer;

import com.example.ShopHop.dto.RequestDto.DirectOrderRequestDto;
import com.example.ShopHop.dto.RequestDto.ItemRequestDto;
import com.example.ShopHop.dto.ResponseDto.ItemResponseDto;
import com.example.ShopHop.model.Item;

public class ItemTransformer {

    public static Item ItemRequestDtoToItem(int quantity){

        return Item.builder()
                .requiredQuantity(quantity)

                .build();
    }

    public static ItemResponseDto ItemToItemResponseDto(Item item) {

        return ItemResponseDto.builder()
                .quatityAdded(item.getRequiredQuantity())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .build();
    }

    public static ItemRequestDto directOrderRequestDtoToItemRequestDto(String email,int productId,int requiredQuantity){
        return ItemRequestDto.builder()
                .customerEmailId(email)
                .productId(productId)
                .requiredQuantity(requiredQuantity)
                .build();
    }
}
