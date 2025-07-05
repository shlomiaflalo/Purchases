package com.johnbryce.coupon_system_final.repositories;

import com.johnbryce.coupon_system_final.entities.CouponPurchaseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponPurchaseLogRepository extends JpaRepository<CouponPurchaseLog, Integer> {
}
