package com.johnbryce.coupon_system_final.services;

import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.security.LoginResponse;

public interface ClientService {
    LoginResponse login(String email, String password) throws CouponSystemException;
}