package com.johnbryce.coupon_system_final.repositories;

import com.johnbryce.coupon_system_final.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM categories ORDER BY RAND()" +
            " LIMIT 1",nativeQuery = true)
    Category findRandomCategory();
    Category findByName(String name);
    boolean existsByIdAndName(int id, String name);
    boolean existsByName(String name);
}
