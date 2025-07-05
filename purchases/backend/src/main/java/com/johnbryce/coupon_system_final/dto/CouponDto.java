package com.johnbryce.coupon_system_final.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDto {

    private int id;
    private CompanyDto company;
    private CategoryDto category;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<PurchaseDto> purchases;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int amount;
    private double price;
    private String image;

}
