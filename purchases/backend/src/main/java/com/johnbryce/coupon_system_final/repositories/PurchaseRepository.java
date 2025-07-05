package com.johnbryce.coupon_system_final.repositories;
import com.johnbryce.coupon_system_final.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {
    List<Purchase> findAllByCustomer_id(int customerId);
    List<Purchase> findAllByCoupon_Company_Id(int companyId);
    List<Purchase> findAllByCoupon_Id(int couponId);
    Purchase findPurchaseByCoupon_idAndCustomer_id(int couponId, int customerId);
    Purchase findFirstByOrderByIdAsc();
    Purchase findFirstByOrderByIdDesc();
    boolean existsPurchaseByCoupon_IdAndCustomer_Id(int couponId, int customerId);
    boolean existsByIdAndCoupon_Company_Id(int id, int companyId);

}