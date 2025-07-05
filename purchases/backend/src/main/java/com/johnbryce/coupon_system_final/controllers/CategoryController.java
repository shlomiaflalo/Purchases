package com.johnbryce.coupon_system_final.controllers;

import com.johnbryce.coupon_system_final.dto.CategoryDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto addCategory(@RequestHeader("Authorization") UUID token,@RequestBody CategoryDto category) throws Exception {
        return categoryService.add(token,category);
    }

    @PutMapping
    public CategoryDto updateCategory(@RequestHeader("Authorization") UUID token,@RequestBody CategoryDto categoryToUpdate) throws Exception {
        return categoryService.update(token,categoryToUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@RequestHeader("Authorization") UUID token,@PathVariable("id") Integer id) throws Exception {
         categoryService.delete(token,id);
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories(@RequestHeader("Authorization") UUID token) throws SQLException, CouponSystemException {
        return categoryService.getAll(token);
    }

    @GetMapping("/{id}")
    public CategoryDto getOneCategory(@RequestHeader("Authorization") UUID token,@PathVariable("id") Integer id) throws Exception {
        return categoryService.getSingle(token,id);
    }

}
