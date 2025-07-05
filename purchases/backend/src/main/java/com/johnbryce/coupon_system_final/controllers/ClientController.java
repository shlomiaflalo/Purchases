package com.johnbryce.coupon_system_final.controllers;

import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.security.LoginManager;
import com.johnbryce.coupon_system_final.security.LoginRequest;
import com.johnbryce.coupon_system_final.security.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/client")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {

    private final LoginManager loginManager;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws CouponSystemException {
        return loginManager.login(loginRequest.getEmail(),loginRequest.getPassword());
    }

}
