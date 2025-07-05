package com.johnbryce.coupon_system_final.controllers;

import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/getAllCustomersByCouponCategory/{categoryId}")
    public List<CustomerDto> getAllCustomersByCouponCategory(@RequestHeader("Authorization") UUID token, @PathVariable("categoryId") int categoryId) throws CouponSystemException {
        return customerService.getAllCustomersByCouponCategory(token,categoryId);
    }

    @PostMapping
    public CustomerDto addCustomer(@RequestHeader("Authorization") UUID token,@RequestBody CustomerDto customerDto) throws Exception {
        return customerService.add(token,customerDto);
    }

    @PutMapping
    public CustomerDto updateCustomer(@RequestHeader("Authorization") UUID token,@RequestBody CustomerDto customerDto) throws CouponSystemException {
        return customerService.update(token,customerDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@RequestHeader("Authorization") UUID token,@PathVariable("id") int id) throws CouponSystemException {
         customerService.delete(token,id);
    }

    @GetMapping("/all")
    public List<CustomerDto> getAllCustomers(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return customerService.getAll(token);
    }

    @GetMapping("/getSingle/{id}")
    public CustomerDto getOneCustomer(@RequestHeader("Authorization") UUID token,@PathVariable("id") int id) throws CouponSystemException {
        return customerService.getSingle(token,id);
    }

}
