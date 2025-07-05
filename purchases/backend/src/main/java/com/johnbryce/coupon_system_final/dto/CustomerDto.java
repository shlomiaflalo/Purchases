package com.johnbryce.coupon_system_final.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<PurchaseDto> purchases;


}
