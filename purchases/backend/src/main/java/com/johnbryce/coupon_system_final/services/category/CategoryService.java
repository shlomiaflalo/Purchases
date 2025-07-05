package com.johnbryce.coupon_system_final.services.category;

import com.johnbryce.coupon_system_final.dto.CategoryDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.TableService;

public interface CategoryService extends TableService<CategoryDto,Integer>  {
    CategoryDto getRandomCategory() throws CouponSystemException;
    boolean isCategoryExistsByName(String name);
    boolean isCategoryExists(CategoryDto categoryDto);
    boolean isCategoryNotChanged(CategoryDto categoryDto) throws CouponSystemException;
}
