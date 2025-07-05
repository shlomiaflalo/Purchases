package com.johnbryce.coupon_system_final.exceptions.purchases;

import com.johnbryce.coupon_system_final.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseException implements ErrorMessage {

    PURCHASE_NOT_EXISTS(1038, "Purchase not exists"),
    PURCHASE_EXISTS(1039, "Purchase already exists"),
    PURCHASE_NOT_EXISTS_BY_CUSTOMER_ID_AND_COUPON_ID(1040,
            "Purchase not exists by customer id and coupon id"),
    PURCHASES_NOT_EXISTS_FOR_THIS_COUPON_ID(1041,
            "Purchases does not exists for this coupon");

    private int code;
    private String message;
}
