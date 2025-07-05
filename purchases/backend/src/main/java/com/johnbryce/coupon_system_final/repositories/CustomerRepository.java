package com.johnbryce.coupon_system_final.repositories;

import com.johnbryce.coupon_system_final.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findFirstByOrderByIdAsc();
    Customer findFirstByOrderByIdDesc();
    Customer findByEmail(String email);
    boolean existsByEmailAndPassword(String email, String password);
    boolean existsByFirstName(String name);
    boolean existsByEmail(String email);
    boolean existsByFirstNameAndId(String name, int customerId);
    boolean existsByEmailAndId(String email, int customerId);
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByFirstNameAndLastNameAndIdNot(String firstName, String lastName, int customerId);
    boolean existsByEmailAndIdNot(String email, int customerId);
}
