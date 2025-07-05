package com.johnbryce.coupon_system_final.services.Admin;

import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.LoginResponse;
import com.johnbryce.coupon_system_final.security.TokenInformation;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.services.company.CompanyService;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    private final String email = "admin@admin.com";
    private final String password = "admin";

    private final CustomerService customerService;
    private final CompanyService companyService;
    private final TokenManager tokenManager;


    public AdminServiceImpl(@Lazy CustomerService customerService,
                            @Lazy CompanyService companyService
                           ,@Lazy TokenManager tokenManager) {
        this.customerService = customerService;
        this.companyService = companyService;
        this.tokenManager = tokenManager;
    }

    @Override
    public LoginResponse login(String email, String password) throws CouponSystemException {
        if (!(email.equalsIgnoreCase(this.email) && password.equalsIgnoreCase(this.password))) {
            throw new CouponSystemException(GenericException.EMAIL_AND_PASSWORD_IS_NOT_CORRECT);
        }
        TokenInformation tokenInformation = new TokenInformation();
        tokenInformation.setId(-1);
        tokenInformation.setEmail(email);
        tokenInformation.setName("Admin");
        tokenInformation.setClientType(ClientType.ADMINISTRATOR);

        UUID newTokenResponse = tokenManager.addToken(tokenInformation);
        return LoginResponse.builder().id(-1).token(newTokenResponse).
                email(email).name("Admin").clientType(ClientType.ADMINISTRATOR).build();
    }

    @Transactional
    @Override
    public CompanyDto addCompany(UUID token, CompanyDto company) throws Exception {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        return companyService.add(token,company);
    }

    @Transactional
    @Override
    public CompanyDto updateCompany(UUID token,CompanyDto company) throws Exception {
        return companyService.update(token,company);
    }

    @Transactional
    @Override
    public void deleteCompany(UUID token,int companyId) throws Exception {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        companyService.delete(token,companyId);
    }

    @Override
    public List<CompanyDto> getAllCompanies(UUID token) throws CouponSystemException {
        try{
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        }catch (CouponSystemException ignored){
        }
        try{
            tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        }catch (CouponSystemException ignored){
        }

        return companyService.getAll(token);
    }

    @Override
    public CompanyDto getOneCompany(UUID token,int companyId) throws CouponSystemException {
        try {
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        } catch (CouponSystemException e) {
            // If not admin, try as company
            tokenManager.getUserIdFromToken(token, ClientType.COMPANY);
        }
        return companyService.getSingle(token,companyId);
    }

    @Transactional
    @Override
    public CustomerDto addCustomer(UUID token,CustomerDto customer) throws Exception {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        return customerService.add(token,customer);
    }

    @Transactional
    @Override
    public CustomerDto updateCustomer(UUID token,CustomerDto customer) throws Exception {
        try {
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        } catch (CouponSystemException e) {
            // If not admin, try as a customer
            tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        }

        return customerService.update(token,customer);
    }

    @Transactional
    @Override
    public void deleteCustomer(UUID token,int customerId) throws Exception {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        customerService.delete(token,customerId);
    }

    @Override
    public List<CustomerDto> getAllCustomers(UUID token) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        return customerService.getAll(token);
    }

    @Override
    public CustomerDto getOneCustomer(UUID token,int customerId) throws CouponSystemException {
        try {
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        } catch (CouponSystemException e) {
            // If not admin, try as customer
            tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        }
        return customerService.getSingle(token,customerId);
    }

    @Override
    public CompanyDto getSingleCompanyByEmail(UUID token,String email) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        return companyService.getSingleByEmail(token,email);
    }

    @Override
    public CustomerDto getSingleCustomerByEmail(UUID token,String email) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        return customerService.getSingleByEmail(token,email);
    }

    @Override
    public CustomerDto getFirstCustomerRecord() throws CouponSystemException, SQLException {
        return customerService.getFirstCustomerRecord();
    }

    @Override
    public CustomerDto getLastCustomerRecord() throws CouponSystemException, SQLException {
        return customerService.getLastCustomerRecord();
    }

    @Override
    public CompanyDto getFirstCompanyRecord() throws CouponSystemException {
        return companyService.getFirstCompanyRecord();
    }

    @Override
    public CompanyDto getLastCompanyRecord() throws CouponSystemException {
        return companyService.getLastCompanyRecord();
    }

}
