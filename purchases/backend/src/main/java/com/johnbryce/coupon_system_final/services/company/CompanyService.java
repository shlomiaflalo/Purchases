package com.johnbryce.coupon_system_final.services.company;

import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.ClientService;
import com.johnbryce.coupon_system_final.services.TableService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CompanyService extends ClientService, TableService<CompanyDto,Integer> {

    CompanyDto getCompany(int id) throws CouponSystemException;
    CompanyDto getRandomCompany() throws CouponSystemException;
    CompanyDto getSingleByEmail(UUID token,String email) throws CouponSystemException;
    CompanyDto getFirstCompanyRecord() throws CouponSystemException;
    CompanyDto getLastCompanyRecord() throws CouponSystemException;
    CompanyDto getSingleInternal(int companyId) throws CouponSystemException;
    boolean isCompanyEmailExistsExclude(String email, int companyId) throws CouponSystemException;
    boolean isCompanyExistsByEmailAndPassword(String email, String password) throws CouponSystemException;
    boolean isCompanyExistsByName(String name) throws CouponSystemException;
    boolean isCompanyExistsByEmail(String email) throws CouponSystemException;
    boolean isCompanyExistsByNameAndId(String name, int id) throws CouponSystemException;
    boolean isCompanyLogin(String email) throws CouponSystemException;
    boolean isCompanyNotChanged(CompanyDto companyDto) throws CouponSystemException;
    void updateCompanyCouponsCountMinus(int companyId) throws CouponSystemException;
    void updateCompanyCouponsCountPlus(int companyId) throws CouponSystemException;
    void deleteCouponByCompanyId(UUID token, int couponId) throws CouponSystemException;
    void saveCompany(CompanyDto companyDto) throws CouponSystemException;

}
