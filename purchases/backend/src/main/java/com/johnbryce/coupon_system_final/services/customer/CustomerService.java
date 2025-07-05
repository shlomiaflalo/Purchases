package com.johnbryce.coupon_system_final.services.customer;

import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.ClientService;
import com.johnbryce.coupon_system_final.services.TableService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface CustomerService extends TableService<CustomerDto,Integer>, ClientService {

    List<CustomerDto> getAllCustomersByCouponCategory(UUID token, int categoryId) throws CouponSystemException;
    CustomerDto getSingleByEmail(UUID token,String email) throws CouponSystemException;
    CustomerDto getFirstCustomerRecord() throws SQLException, CouponSystemException;
    CustomerDto getLastCustomerRecord() throws SQLException, CouponSystemException;
    boolean isCustomerEmailExistsExclude(String email, int customerId) throws CouponSystemException;
    boolean isCustomerEmailExistsById(String email, int customerId) throws CouponSystemException;
    boolean isCustomerEmailExists(String email) throws CouponSystemException;
    boolean isCustomerExists(String email, String password) throws CouponSystemException;
    boolean isCustomerNotChanged(CustomerDto customerDto) throws CouponSystemException;
    void removePurchasesByCouponId(int couponId) throws CouponSystemException, SQLException;
    void removePurchasesByCompanyId(UUID token) throws CouponSystemException, SQLException;
    void cleanPurchaseListByCustomerId(int customerId) throws CouponSystemException;
    void insertOrUpdateCustomerAfterLogic(CustomerDto customer) throws CouponSystemException;

}
