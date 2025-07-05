package com.johnbryce.coupon_system_final.exceptions.generic;

import com.johnbryce.coupon_system_final.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenericException implements ErrorMessage {

    EMAIL_IS_NOT_CORRECT(1026, "Email is not correct"),
    EMAIL_IS_NOT_FOUND(1027, "Email is not found"),
    EMAIL_AND_PASSWORD_IS_NOT_CORRECT(1028, "Email and password is not correct"),
    PASSWORD_IS_NOT_CORRECT(1029, "Password is not correct"),
    USELESS_EDITING(1030, "Useless editing - you cannot" +
            " edit your info with same information as before"),
    TOKEN_CREATION_GOT_WRONG(1031, "Token information is wrong - null"),
    INVALID_TOKEN(1032, "Token is invalid and therefore it cannot proceed"),
    UNAUTHORIZED_ACTION(1033, "Unauthorized action - cannot proceed"),
    NO_CHANGES_HAS_BEEN_DONE(1034, "You did not change any details,This is the Same object as before - it cannot get update"),
    UNKNOWN_OBJECT_TYPE(1035, "Unknown object type"),
    EMAIL_BELONG_TO_ADMIN(1036,"An admin account with this email address already exists"),
    STARTING_DATE_MUST_BE_BEFORE_ITS_END(1037,"The start date should be set before the end date");


    private final int code;
    private final String message;


}
