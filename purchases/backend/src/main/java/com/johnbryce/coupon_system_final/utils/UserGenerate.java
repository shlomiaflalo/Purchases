package com.johnbryce.coupon_system_final.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGenerate {

    String firstName;
    String lastName;
    String email;
}
