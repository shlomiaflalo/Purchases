package com.johnbryce.coupon_system_final.security;

import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.Admin.AdminService;
import com.johnbryce.coupon_system_final.services.company.CompanyService;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LoginManager {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final CompanyService companyService;

    public LoginResponse login(String email, String password) throws CouponSystemException {
        ClientType clientType = email.equalsIgnoreCase("admin@admin.com") ? ClientType.ADMINISTRATOR
                : companyService.isCompanyLogin(email) ? ClientType.COMPANY : ClientType.CUSTOMER;
        return switch (clientType) {
            case ADMINISTRATOR -> adminService.login(email, password);
            case COMPANY -> companyService.login(email, password);
            case CUSTOMER -> customerService.login(email, password);
        };
    }
}
