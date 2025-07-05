package com.johnbryce.coupon_system_final.services.coupon;

import com.johnbryce.coupon_system_final.dto.CouponDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.TableService;

import java.util.List;
import java.util.UUID;

public interface CouponService extends TableService<CouponDto, Integer> {

    List<CouponDto> getCompanyCouponsByCompany(UUID token) throws CouponSystemException;
    List<CouponDto> getCompanyCouponsByCompany(UUID token, double maxPrice) throws CouponSystemException;
    List<CouponDto> getCompanyCouponsByCategoryIdAndByCompany(UUID token, int categoryId) throws CouponSystemException;
    List<CouponDto> getCompanyCouponsByCustomer(UUID token,int companyId) throws CouponSystemException;
    List<CouponDto> getCompanyCouponsByCustomer(UUID token, double maxPrice,int companyId) throws CouponSystemException;
    List<CouponDto> getCompanyCouponsByCategoryIdAndByCustomer(UUID token, int categoryId,int companyId) throws CouponSystemException;
    List<CouponDto> getCustomerCoupons(UUID token) throws CouponSystemException;
    List<CouponDto> getCustomerCouponsByCategoryId(UUID token, int categoryId) throws CouponSystemException;
    List<CouponDto> getCustomerCouponsByMaxPrice(UUID token,double maxPrice) throws CouponSystemException;
    CouponDto getLastRecordCoupon() throws CouponSystemException;
    CouponDto getFirstRecordCoupon() throws CouponSystemException;
    CouponDto addCouponByCompany(UUID token, CouponDto coupon) throws CouponSystemException;
    CouponDto updateCouponByCompany(UUID token, CouponDto coupon) throws CouponSystemException;
    boolean isCouponAvailable(int couponId) throws CouponSystemException;
    boolean dueDateCouponCheck(int couponId) throws CouponSystemException;
    boolean isCouponTitleExistsByCompanyId(int companyId, String title) throws CouponSystemException;
    boolean isCouponExistsByCouponIdAndCompanyId(int couponId, int companyId) throws CouponSystemException;
    boolean isCouponTitleExistsByCompanyIdExclude(int companyId, String title,int CouponId) throws CouponSystemException;
    boolean isCouponTypePurchased(int customerId, int couponId) throws CouponSystemException;
    boolean isCouponNotChanged(CouponDto couponDto) throws CouponSystemException;
    void updateCouponAmountAndPurchased(int couponId) throws CouponSystemException;
    void deleteCouponByCompany(UUID token, int couponId) throws CouponSystemException;
    void removeAllExpiredCoupons() throws CouponSystemException;
    void updatePreviousPurchase(int couponId);
}

