package com.johnbryce.coupon_system_final.exceptions.companies;

import com.johnbryce.coupon_system_final.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyException implements ErrorMessage {

    COMPANY_NOT_FOUND(1010, "Company does not exists"),
    COMPANY_ID_ALREADY_EXISTS(1011, "Impossible to change company id"),
    COMPANY_NAME_ALREADY_EXISTS(1012, "Company name already exists"),
    COMPANY_EMAIL_ALREADY_EXISTS(1013, "Company email already exists"),
    CANNOT_EDIT_COMPANY_NAME_AND_ID(1014, "Company name and id cannot be change");

    private final int code;
    private final String message;

}
