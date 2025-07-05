package com.johnbryce.coupon_system_final.controllers;

import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.Admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController  {

    private final AdminService adminService;

    @PostMapping("/company")
    public CompanyDto addCompany(@RequestHeader("Authorization") UUID token,@RequestBody CompanyDto companyDto)
            throws Exception {
        return adminService.addCompany(token,companyDto);
    }

    @PutMapping("/company")
    public CompanyDto updateCompany(@RequestHeader("Authorization") UUID token,@RequestBody CompanyDto company) throws Exception {
        return adminService.updateCompany(token,company);
    }

    @DeleteMapping("/company/{companyId}")
    public void deleteCompany(@RequestHeader("Authorization") UUID token,@PathVariable("companyId") int companyId)
            throws Exception {
         adminService.deleteCompany(token,companyId);
    }

    @GetMapping("/getAllCompanies")
    public List<CompanyDto> getAllCompanies(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return adminService.getAllCompanies(token);
    }

    @GetMapping("/getOneCompany/{companyId}")
    public CompanyDto getOneCompany(@RequestHeader("Authorization") UUID token,@PathVariable("companyId") int companyId) throws Exception {
        return adminService.getOneCompany(token,companyId);
    }

    @PostMapping("/customer")
    public CustomerDto addCustomer(@RequestHeader("Authorization") UUID token,@RequestBody CustomerDto customer) throws Exception {
        return adminService.addCustomer(token,customer);
    }

    @PutMapping("/customer")
    public CustomerDto updateCustomer(@RequestHeader("Authorization") UUID token,@RequestBody CustomerDto customer) throws Exception {
        return adminService.updateCustomer(token,customer);
    }

    @DeleteMapping("/customer/{customerId}")
    public void deleteCustomer(@RequestHeader("Authorization") UUID token,@PathVariable("customerId") int customerId) throws Exception {
         adminService.deleteCustomer(token,customerId);
    }

    @GetMapping("/getAllCustomers")
    public List<CustomerDto> getAllCustomers(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return adminService.getAllCustomers(token);
    }

    @GetMapping("/getOneCustomer/{customerId}")
    public CustomerDto getOneCustomer(@RequestHeader("Authorization") UUID token,@PathVariable("customerId") int customerId) throws Exception {
        return adminService.getOneCustomer(token,customerId);
    }

    @GetMapping("/getSingleCompanyByEmail")
    public CompanyDto getCompanyByEmail(@RequestHeader("Authorization") UUID token,@RequestParam("companyEmail") String companyEmail) throws CouponSystemException {
        return adminService.getSingleCompanyByEmail(token,companyEmail);
    }

    @GetMapping("/getSingleCustomerByEmail")
    public CustomerDto getCustomerByEmail(@RequestHeader("Authorization") UUID token,@RequestParam("customerEmail") String customerEmail) throws CouponSystemException {
        return adminService.getSingleCustomerByEmail(token,customerEmail);
    }

}
