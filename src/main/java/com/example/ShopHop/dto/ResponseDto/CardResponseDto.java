package com.example.ShopHop.dto.ResponseDto;

import com.example.ShopHop.Enum.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardResponseDto {

    String customerName;

    String cardNo;

    Date expiryDate;

    CardType cardType;

    String cvv;

    String emailId;

}
