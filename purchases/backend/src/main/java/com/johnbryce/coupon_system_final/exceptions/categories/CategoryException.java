package com.johnbryce.coupon_system_final.exceptions.categories;

import com.johnbryce.coupon_system_final.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryException implements ErrorMessage {

    CATEGORY_ALREADY_EXISTS(1022, "Category already exists"),
    CATEGORY_NOT_EXISTS(1023, "Category does not exist"),
    CATEGORY_EXISTS_BY_ID(1024, "Category exists by id"),
    CATEGORY_NOT_EXISTS_BY_ID(1025, "Category not exists by id");

    private int code;
    private String message;

}
