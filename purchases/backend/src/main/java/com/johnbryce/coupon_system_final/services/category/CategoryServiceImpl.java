package com.johnbryce.coupon_system_final.services.category;

import com.johnbryce.coupon_system_final.dto.CategoryDto;
import com.johnbryce.coupon_system_final.entities.Category;
import com.johnbryce.coupon_system_final.exceptions.categories.CategoryException;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_final.repositories.CategoryRepository;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TokenManager tokenManager;
    private final Utils utils;

    @Override
    public CategoryDto getRandomCategory() throws CouponSystemException {
        CategoryDto categoryDto =
                utils.convertToDto(categoryRepository.findRandomCategory(), CategoryDto.class);
        if (categoryDto == null) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS);
        }
        return categoryDto;
    }


    @Transactional
    @Override
    public CategoryDto add(UUID token,CategoryDto categoryDto) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (categoryRepository.existsById(categoryDto.getId())) {
            throw new CouponSystemException(CategoryException.CATEGORY_ALREADY_EXISTS);
        }

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new CouponSystemException(CategoryException.CATEGORY_ALREADY_EXISTS);
        }

        Category categoryConversion = utils.convertFromDto(categoryDto, Category.class);
        Category categoryInDb = categoryRepository.save(categoryConversion);
        return utils.convertToDto(categoryInDb, CategoryDto.class);
    }

    @Transactional
    @Override
    public CategoryDto update(UUID token,CategoryDto categoryDto) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (!categoryRepository.existsById(categoryDto.getId())) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS);
        }

        if(isCategoryNotChanged(categoryDto)){
            throw new CouponSystemException(GenericException.NO_CHANGES_HAS_BEEN_DONE);
        }

        Category category = utils.convertFromDto(categoryDto, Category.class);
        Category categoryInDb = categoryRepository.save(category);
        return utils.convertToDto(categoryInDb, CategoryDto.class);
    }

    @Transactional
    @Override
    public void delete(UUID token,Integer id) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        categoryRepository.findById(id).orElseThrow(() ->
                new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS));

        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAll(UUID token) throws CouponSystemException {
        try{
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        }catch (CouponSystemException ignored){
        }
        try{
            tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        }catch (CouponSystemException ignored){
        }
        try{
            tokenManager.getUserIdFromToken(token, ClientType.COMPANY);
        }catch (CouponSystemException ignored){
        }

        return utils.convertFromEntityListToDto(categoryRepository.findAll(), CategoryDto.class);
    }

    @Override
    public CategoryDto getSingle(UUID token,Integer integer) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        Category category = categoryRepository.findById(integer).orElseThrow(() ->
                new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS));

        return utils.convertToDto(category, CategoryDto.class);
    }

    @Override
    public boolean isExists(Integer integer) {
        return categoryRepository.existsById(integer);
    }

    @Override
    public boolean isCategoryExists(CategoryDto categoryDto) {
        return categoryRepository.existsByIdAndName(categoryDto.getId(), categoryDto.getName());
    }

    @Override
    public boolean isCategoryExistsByName(String name) {
       return categoryRepository.existsByName(name);
        }

    @Override
    public boolean isCategoryNotChanged(CategoryDto categoryDto) throws CouponSystemException {

        //Updatable Category details
        String currentCategoryName = categoryDto.getName();

        //Current Category details
        String previousCategoryName = categoryRepository.getReferenceById(categoryDto.getId()).getName();

        return currentCategoryName.equals(previousCategoryName);
    }
}
