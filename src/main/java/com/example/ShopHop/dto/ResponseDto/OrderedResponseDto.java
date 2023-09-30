package com.example.ShopHop.dto.ResponseDto;

import com.example.ShopHop.Enum.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderedResponseDto {

    String customerName;

    String orderNo;

    int noOfItems;

    int totalOrderValue;

    Date orderDate;

    String cardUsed;

    List<ItemResponseDto> itemResponseList;

    OrderStatus orderStatus;
}

