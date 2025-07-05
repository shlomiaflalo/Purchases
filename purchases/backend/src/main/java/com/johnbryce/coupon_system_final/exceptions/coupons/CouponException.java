package com.johnbryce.coupon_system_final.exceptions.coupons;

import com.johnbryce.coupon_system_final.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CouponException implements ErrorMessage {

    COUPON_ALREADY_EXISTS(1000, "Coupon already exists"),
    COUPON_NOT_EXISTS(1001, "Coupon not exists"),
    NO_COUPONS_FOR_CUSTOMER(1002, "Customer do not have any coupons"),
    NO_COUPONS_FOR_CUSTOMER_BY_CATEGORY(1003, "Customer do not have any coupons order by category"),
    DUE_DATE_COUPON(1004, "Unfortunately, The due date of this coupon is over and therefore you can't purchase it"),
    COUPON_IS_OUT_OF_STOCK(1005, "Coupon is out of stock and its amount is 0"),
    COUPON_TITLE_EXISTS_FOR_THIS_COMPANY(1006, "A company can't have same title for its coupons"),
    CANNOT_EDIT_COUPON_OF_ANOTHER_COMPANY(1007, "Coupon is belonging to another company and you can't update it"),
    COUPON_EXISTS_BY_TITLE_AND_DESCRIPTION(1008, "Coupon exists By Title And Description"),
    COUPON_MAX_PRICE_IS_OUT_OF_RANGE(1009, "Coupon max price can't be lower or equal to 0 or bigger than 100k");

    private int code;
    private String message;


}
