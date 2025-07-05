package com.johnbryce.coupon_system_final.db;


import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.TokenInformation;
import com.johnbryce.coupon_system_final.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminTokenInternalUse {

    private final TokenManager tokenManager;

    @Bean
    public UUID getAdminToken() throws CouponSystemException {
        TokenInformation adminToken = new TokenInformation();
        adminToken.setId(-1);
        adminToken.setEmail("admin@admin.com");
        adminToken.setName("Admin");
        adminToken.setClientType(ClientType.ADMINISTRATOR);
        return tokenManager.addToken(adminToken);
    }
}
