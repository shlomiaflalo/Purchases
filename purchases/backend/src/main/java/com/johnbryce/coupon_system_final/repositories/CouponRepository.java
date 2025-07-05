package com.johnbryce.coupon_system_final.repositories;

import com.johnbryce.coupon_system_final.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    @Transactional
    @Modifying
    @Query(value =  "DELETE FROM couponSystemFinal.purchases"+
            " WHERE coupon_id IN (SELECT id FROM coupons WHERE end_date < CURRENT_DATE());" +
            " DELETE FROM coupons WHERE end_date < CURRENT_DATE()",
            nativeQuery = true)
    void removeAllExpiredCoupons();
    @Transactional
    @Modifying
    @Query(value = "UPDATE couponSystemFinal.coupons AS c SET" +
            " c.purchased = c.purchased + 1, c.amount= c.amount-1" +
            " ,c.updated_at = CURRENT_TIMESTAMP WHERE c.id=:couponId"
            ,nativeQuery = true)
    void updateCouponAmountAndPurchased(@Param("couponId") int couponId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE couponSystemFinal.coupons AS c SET" +
            " c.purchased = c.purchased - 1, c.amount= c.amount+1" +
            " ,c.updated_at = CURRENT_TIMESTAMP WHERE c.id=:couponId"
            ,nativeQuery = true)
    void updatePreviousPurchase(@Param("couponId") int couponId);
    void removeAllByCompany_Id(int companyId);
    boolean existsByCompany_Id(int companyId);
    boolean existsByCompanyIdAndTitle(int companyId, String title);
    boolean existsByIdAndCompany_Id(int id, int companyId);
    boolean existsByCompany_idAndTitleAndIdNot(int companyId, String title, int id);
    boolean existsByTitleAndDescription(String title, String description);
    boolean existsByTitleAndDescriptionAndIdNot(String title, String description, int id);
    List<Coupon>findAllByCompanyIdAndPriceIsLessThanEqual(int companyId, double price);
    List<Coupon> findAllByCompany_IdAndCategory_Id(int companyId, int categoryId);
    List<Coupon> findAllByCompany_Id(int companyId);
    List<Coupon>findAllByEndDateLessThan(LocalDate endDate);
    Coupon findFirstByOrderByIdAsc();
    Coupon findFirstByOrderByIdDesc();




}