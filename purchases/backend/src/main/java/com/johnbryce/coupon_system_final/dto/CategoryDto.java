package com.johnbryce.coupon_system_final.dto;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private int id;
    private String name;

}