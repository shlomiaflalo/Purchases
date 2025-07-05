package com.johnbryce.coupon_system_final.exceptions.customers;

import com.johnbryce.coupon_system_final.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerException implements ErrorMessage {

    FIRST_AND_LAST_NAME_IN_USE_BY_ANOTHER_CUSTOMER(1015, "Another customers having this first and last name already"),
    CUSTOMER_BOUGHT_THIS_COUPON_BEFORE(1016, "Customer already bought this coupon"),
    CUSTOMER_IS_NOT_EXISTS(1017, "customer is not exists"),
    CUSTOMER_EMAIL_DO_NOT_MATCH_TO_CUSTOMER_ID(1018, "No matching between customer id and existing email"),
    CUSTOMER_IS_ALREADY_EXISTS(1019, "customer is already exists"),
    EMAIL_IN_USE_BY_ANOTHER_CUSTOMER(1020, "Another customer using this email address already"),
    CUSTOMER_BOUGHT_THIS_COUPON_TYPE_BEFORE(1021, "Customer already have this coupon type and can buy just 1 coupon from every category");

    private int code;
    private String message;


}
