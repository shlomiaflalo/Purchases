package com.johnbryce.coupon_system_final.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginResponse {

    //UUID - unique string for token - like JWT
    private UUID token;
    private Integer id;
    private String email;
    private String name;
    private ClientType clientType;
    @Builder.Default
    private LocalDateTime expireTime = LocalDateTime.now().plusMinutes(30);
}
