package com.example.ShopHop.dto.RequestDto;

import com.example.ShopHop.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CustomerRequestDto {

        String name;

        String dob;

        String mobNo;

        String emailId;

        String password;

    //    Gender gender;

        String address;

    }
