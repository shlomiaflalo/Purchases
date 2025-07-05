package com.johnbryce.coupon_system_final.dto;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDto {

    private int id;
    private CustomerDto customer;
    private CouponDto coupon;


}
