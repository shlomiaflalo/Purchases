package com.johnbryce.coupon_system_final.controllers;

import com.johnbryce.coupon_system_final.dto.CouponDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/coupon")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/getCompanyCouponsByCompany")
    public List<CouponDto> getCompanyCouponsByCompany(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return couponService.getCompanyCouponsByCompany(token);
    }

    @GetMapping("/getCompanyCouponsByCustomer/{companyId}")
    public List<CouponDto> getCompanyCouponsByCustomer(@RequestHeader("Authorization") UUID token,@PathVariable("companyId") int companyId) throws CouponSystemException {
        return couponService.getCompanyCouponsByCustomer(token,companyId);
    }

    @GetMapping("/getCompanyCouponsByMaxPriceByCompany")
    public List<CouponDto> getCompanyCouponsByCompany(@RequestHeader("Authorization") UUID token,
                                             @RequestParam("maxPrice") double maxPrice) throws CouponSystemException {
        return couponService.getCompanyCouponsByCompany(token, maxPrice);
    }

    @GetMapping("/getCompanyCouponsByMaxPriceByCustomer/{companyId}")
    public List<CouponDto> getCompanyCouponsByCustomer(@RequestHeader("Authorization") UUID token,
                                                       @PathVariable("companyId") int companyId,
                                                      @RequestParam("maxPrice") double maxPrice) throws CouponSystemException {
        return couponService.getCompanyCouponsByCustomer(token, maxPrice,companyId);
    }

    @GetMapping("/getCouponsByMaxPriceAndCustomerId")
    public List<CouponDto> getCustomerCouponsByMaxPrice(@RequestHeader("Authorization") UUID token,@RequestParam("maxPrice")
                                                             double maxPrice) throws CouponSystemException {
        return couponService.getCustomerCouponsByMaxPrice(token, maxPrice);
    }

    @GetMapping("/getCustomerCouponsByCategoryId/{categoryId}")
    public List<CouponDto> getCustomerCouponsByCategoryId(@RequestHeader("Authorization") UUID token,@PathVariable("categoryId") int categoryId)
            throws CouponSystemException {
        return couponService.getCustomerCouponsByCategoryId(token, categoryId);
    }


    @GetMapping("/getCustomerCoupons")
    public List<CouponDto> getCustomerCoupons(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return couponService.getCustomerCoupons(token);
    }

    @PostMapping("/addCouponByCompany")
    public CouponDto addCouponByCompany(@RequestHeader("Authorization") UUID token,
                                      @RequestBody CouponDto coupon) throws Exception {
        return couponService.addCouponByCompany(token, coupon);
    }


    @PutMapping("/updateCouponByCompany")
    public CouponDto updateCouponByCompany(@RequestHeader("Authorization") UUID token, @RequestBody CouponDto coupon) throws Exception {
        return couponService.updateCouponByCompany(token, coupon);
    }

    @DeleteMapping("/deleteCouponByCompany/{couponId}")
    public void deleteCouponByCompany(@RequestHeader("Authorization") UUID token, @PathVariable("couponId") int couponId) throws CouponSystemException {
         couponService.deleteCouponByCompany(token, couponId);
    }

    @GetMapping("/getCompanyCouponsByCategoryIdAndByCompany/{categoryId}")
    public List<CouponDto> getCompanyCouponsByCategoryIdAndByCompany
            (@RequestHeader("Authorization") UUID token, @PathVariable("categoryId") int categoryId) throws CouponSystemException {
        return couponService.getCompanyCouponsByCategoryIdAndByCompany(token, categoryId);
    }

    @GetMapping("/getCompanyCouponsByCategoryIdAndByCustomer/{companyId}/{categoryId}")
    public List<CouponDto> getCompanyCouponsByCategoryIdAndByCustomer
            (@RequestHeader("Authorization") UUID token, @PathVariable("categoryId") int categoryId, @PathVariable("companyId") int companyId) throws CouponSystemException {
        return couponService.getCompanyCouponsByCategoryIdAndByCustomer(token, categoryId,companyId);
    }

    @GetMapping("/all")
    public List<CouponDto> getAllCoupons(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return couponService.getAll(token);
    }

    @GetMapping("/{couponId}")
    public CouponDto getOneCoupon(@RequestHeader("Authorization") UUID token,@PathVariable("couponId") int couponId) throws Exception {
        return couponService.getSingle(token,couponId);
    }

}
