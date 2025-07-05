package com.johnbryce.coupon_system_final.coupons_cleaner;

import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponExpirationDailyJob {

    private final CouponService couponService;

    @Scheduled(cron = "0 0 0 * * *")
    public void removeAllExpiredCoupons() throws CouponSystemException {
        couponService.removeAllExpiredCoupons();
    }

}
