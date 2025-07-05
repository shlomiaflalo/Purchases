package com.johnbryce.coupon_system_final.repositories;

import com.johnbryce.coupon_system_final.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query(value = "SELECT * FROM companies ORDER BY RAND()" +
            " LIMIT 1",nativeQuery = true)
    Company findRandomCompany();
    Company findFirstByOrderByIdAsc();
    Company findFirstByOrderByIdDesc();
    Company findByEmail(String email);
    boolean existsByEmailAndIdNot(String email, int companyId);
    boolean existsByEmailAndPassword(String email, String password);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsByNameAndId(String name, int id);

}
