package com.johnbryce.coupon_system_final.services.Admin;

import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.ClientService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface AdminService extends ClientService {

    List<CompanyDto> getAllCompanies(UUID token) throws CouponSystemException;
    List<CustomerDto> getAllCustomers(UUID token) throws CouponSystemException;
    CompanyDto getSingleCompanyByEmail(UUID token,String email) throws CouponSystemException;
    CompanyDto updateCompany(UUID token,CompanyDto company) throws Exception;
    CompanyDto getFirstCompanyRecord() throws CouponSystemException;
    CompanyDto getLastCompanyRecord() throws CouponSystemException;
    CompanyDto addCompany(UUID token,CompanyDto company) throws Exception;
    CompanyDto getOneCompany(UUID token,int companyId) throws CouponSystemException;
    CustomerDto updateCustomer(UUID token,CustomerDto customer) throws Exception;
    CustomerDto getSingleCustomerByEmail(UUID token,String email) throws CouponSystemException;
    CustomerDto getLastCustomerRecord() throws CouponSystemException, SQLException;
    CustomerDto getFirstCustomerRecord() throws CouponSystemException, SQLException;
    CustomerDto addCustomer(UUID token,CustomerDto customer) throws Exception;
    CustomerDto getOneCustomer(UUID token,int customerId) throws CouponSystemException;
    void deleteCompany(UUID token,int companyId) throws Exception;
    void deleteCustomer(UUID token,int customerId) throws Exception;
}
