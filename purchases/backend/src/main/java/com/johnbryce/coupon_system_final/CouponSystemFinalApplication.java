package com.johnbryce.coupon_system_final;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CouponSystemFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponSystemFinalApplication.class, args);
        System.out.println("\nServer is running");
    }

}
