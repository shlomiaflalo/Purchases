package com.johnbryce.coupon_system_final.services;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;

import java.util.List;
import java.util.UUID;

public interface TableService<T, ID>{

    T add(UUID token, T t) throws Exception;
    T getSingle(UUID token,ID id) throws CouponSystemException;
    T update(UUID token,T updateRecord) throws CouponSystemException;
    List<T> getAll(UUID token) throws CouponSystemException;
    void delete(UUID token,ID id) throws CouponSystemException;
    boolean isExists(ID id);
}
