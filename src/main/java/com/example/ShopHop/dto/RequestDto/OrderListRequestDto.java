package com.example.ShopHop.dto.RequestDto;

import com.example.ShopHop.dto.ResponseDto.ProductResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderListRequestDto {
    String customerEmailId;
    String  cardNo;
    String cvv;
    List<ItemRequestDto> orderItems=new ArrayList<>();
}

