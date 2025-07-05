package com.johnbryce.coupon_system_final.services.purchase;

import com.johnbryce.coupon_system_final.dto.PurchaseDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.TableService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface PurchaseService extends TableService<PurchaseDto,Integer> {
    List<PurchaseDto> getCustomerPurchases(int customerId) throws CouponSystemException;
    void deleteCouponPurchase(UUID token,int customerId, int couponId) throws CouponSystemException;
    void deleteCouponPurchasesByCouponId(UUID token,int couponId) throws CouponSystemException, SQLException;
    void deleteCouponPurchasesByCompanyId(UUID token) throws CouponSystemException, SQLException;
    void deleteCouponPurchasesByCustomerId(UUID token,int customerId) throws CouponSystemException;
    void updatePurchaseViaCustomer(UUID token, PurchaseDto purchaseDto) throws CouponSystemException;
    void addPurchaseViaCustomer(UUID token, PurchaseDto purchaseDto) throws Exception;
    void couponTestBeforeUpdatePurchase(UUID token, int couponId) throws CouponSystemException;
    void couponTestBeforePurchase(UUID token, int couponId) throws Exception;
    void purchaseCoupon(UUID token, int couponId) throws Exception;
    boolean isPurchaseNotChanged(PurchaseDto purchaseToUpdate) throws CouponSystemException;
    boolean isCouponPurchasedByCustomer(int customerId, int couponId) throws CouponSystemException;

}
