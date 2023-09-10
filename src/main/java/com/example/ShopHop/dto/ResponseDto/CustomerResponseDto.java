package com.example.ShopHop.dto.ResponseDto;

import com.example.ShopHop.Enum.Gender;
import com.example.ShopHop.model.Card;
import com.example.ShopHop.model.Cart;
import com.example.ShopHop.model.Ordered;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    public class CustomerResponseDto {

        int id;

        String name;

        String dob;

        String mobNo;

        String emailId;

        Gender gender;

        String address;

        List<Card> cards;

        Cart cart;

        List<Ordered> orderedList;

    }


