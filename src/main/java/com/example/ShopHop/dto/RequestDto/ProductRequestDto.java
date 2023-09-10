package com.example.ShopHop.dto.RequestDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder

    public class ProductRequestDto {



        String name;

        int quantity;

        int price;

        String productCategory;

}
