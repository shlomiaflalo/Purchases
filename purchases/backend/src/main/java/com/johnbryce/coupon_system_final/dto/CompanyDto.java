package com.johnbryce.coupon_system_final.dto;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

    private int id;
    private String name;
    private String email;
    private String password;
    private int couponCount;

}

