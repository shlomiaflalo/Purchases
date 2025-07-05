package com.johnbryce.coupon_system_final.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CouponSystemAdviser {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = CouponSystemException.class)
    public Error handleCouponException(
            CouponSystemException exception) {
        int code = exception.getErrorMessage().getCode();
        String message = exception.getErrorMessage().getMessage();
        return Error.builder().code(code).message(message).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public Error handleAnyException(){
        int code = 400;
        String message = "Something went wrong while processing a request";
        return Error.builder().code(code).message(message).build();
    }

}
