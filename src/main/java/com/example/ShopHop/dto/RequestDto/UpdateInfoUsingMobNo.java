package com.example.ShopHop.dto.RequestDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoUsingMobNo {

    String mobNo;

    String newName;

    String newDob;

    String newEmailId;

    String newAddress;

    String password;
}
