package com.johnbryce.coupon_system_final.log;

import com.johnbryce.coupon_system_final.db.AdminTokenInternalUse;
import com.johnbryce.coupon_system_final.dto.CouponDto;
import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.entities.CouponPurchaseLog;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.repositories.CouponPurchaseLogRepository;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.services.coupon.CouponService;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class CouponLoggerAspect {

    private final CouponPurchaseLogRepository purchaseLogRepository;
    private final CustomerService customerService;
    private final CouponService couponService;
    private final AdminTokenInternalUse adminTokenInternalUse;
    private final TokenManager tokenManager;

    @After("@annotation(com.johnbryce.coupon_system_final.log.LogCoupon)")
    @Transactional
    public void logCouponPurchase(JoinPoint joinPoint) throws CouponSystemException {
        Object[] args = joinPoint.getArgs();

        UUID token = null;
        int couponId = -1;

        for (Object arg : args) {
            if (arg instanceof UUID uuid) {
                token = uuid;
            } else if (arg instanceof Integer id) {
                couponId = id;
            }
        }

        UUID adminToken = adminTokenInternalUse.getAdminToken();
        tokenManager.getUserIdFromToken(adminToken, ClientType.ADMINISTRATOR);

        int customerId = tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        CustomerDto customer = customerService.
                getSingle(adminToken,customerId);

        CouponDto coupon = couponService.getSingle(token,couponId);

        CouponPurchaseLog log = CouponPurchaseLog.builder()
                .title(coupon.getTitle())
                .company(coupon.getCompany().getName())
                .category(coupon.getCategory().getName())
                .description(coupon.getDescription())
                .purchaseDate(LocalDate.now())
                .customerFirstName(customer.getFirstName())
                .customerLastName(customer.getLastName())
                .token(token)
                .build();

        purchaseLogRepository.save(log);
    }

}
