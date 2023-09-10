package com.example.ShopHop.dto.ResponseDto;

import com.example.ShopHop.Enum.ProductCategory;
import com.example.ShopHop.Enum.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class ProductResponseDto {

    int id;

    String productName;

    int quantity;

    double price;

    String productCategory;

    ProductStatus productStatus;


}
