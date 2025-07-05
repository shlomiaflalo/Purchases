package com.johnbryce.coupon_system_final.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TokenInformation {
    private Integer id;
    private String email;
    private String name;
    private ClientType clientType;
    private LocalDateTime expireTime = LocalDateTime.now().plusMinutes(30);
}
