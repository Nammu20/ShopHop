package com.example.ShopHop.dto.RequestDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.ArrayList;

@NoArgsConstructor
    @AllArgsConstructor
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class DirectOrderRequestDto {

        String customerEmailId;

        int productId;

        int requiredQuantity;

        String  cardNo;

        String cvv;
    }



