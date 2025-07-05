package com.johnbryce.coupon_system_final.controllers;

import com.johnbryce.coupon_system_final.log.LogCoupon;
import com.johnbryce.coupon_system_final.dto.PurchaseDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/purchase")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/{couponId}")
    //Using AOP to register each purchase on "CouponPurchaseLog"
    @LogCoupon()
    public void purchaseCoupon
            (@RequestHeader("Authorization") UUID token,@PathVariable("couponId") int couponId) throws Exception {
         purchaseService.purchaseCoupon(token, couponId);
    }

    @DeleteMapping("/deleteCouponPurchase/{customerId}/{couponId}")
    public void deleteCouponPurchase
            (@RequestHeader("Authorization") UUID token,@PathVariable("customerId") int customerId,
             @PathVariable("couponId") int couponId) throws Exception {
         purchaseService.deleteCouponPurchase(token,customerId, couponId);
    }


    @DeleteMapping("/deleteCouponPurchasesByCouponId/{couponId}")
    public void deleteCouponPurchasesByCouponId(@RequestHeader("Authorization") UUID token,@PathVariable("couponId") int couponId) throws Exception {
         purchaseService.deleteCouponPurchasesByCouponId(token,couponId);
    }


    @DeleteMapping("/deleteCouponPurchaseByCompanyId")
    public void deleteCouponPurchaseByCompanyId(@RequestHeader("Authorization") UUID token) throws Exception {
         purchaseService.deleteCouponPurchasesByCompanyId(token);
    }

    @DeleteMapping("/deleteCouponPurchaseByCustomerId/{customerId}")
    public void deleteCouponPurchaseByCustomerId(@RequestHeader("Authorization") UUID token,@PathVariable("customerId") int customerId) throws CouponSystemException {
         purchaseService.deleteCouponPurchasesByCustomerId(token,customerId);
    }

    @PutMapping
    public PurchaseDto updatePurchase(@RequestHeader("Authorization") UUID token,@RequestBody PurchaseDto purchaseDto) throws Exception {
        return purchaseService.update(token,purchaseDto);
    }

    @GetMapping("/all")
    public List<PurchaseDto> getAllPurchases(@RequestHeader("Authorization") UUID token) throws SQLException, CouponSystemException {
        return purchaseService.getAll(token);
    }

    @GetMapping("/{id}")
    public PurchaseDto getOnePurchase(@RequestHeader("Authorization") UUID token,@PathVariable("id") int id) throws Exception {
        return purchaseService.getSingle(token,id);
    }

}
