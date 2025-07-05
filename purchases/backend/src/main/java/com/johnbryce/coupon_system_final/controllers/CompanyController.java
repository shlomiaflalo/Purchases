package com.johnbryce.coupon_system_final.controllers;

import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.services.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/company")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CompanyController {

    private final CompanyService companyService;


    @PostMapping
    public CompanyDto addCompany(@RequestHeader("Authorization") UUID token, @RequestBody CompanyDto companyDto)
            throws Exception {
        return companyService.add(token,companyDto);
    }


    @GetMapping("/getSingleByEmail")
    public CompanyDto getCompanyByEmail(@RequestHeader("Authorization") UUID token,@RequestParam("email") String email) throws CouponSystemException {
        return companyService.getSingleByEmail(token,email);
    }


    @PutMapping
    public CompanyDto updateCompany(@RequestHeader("Authorization") UUID token,@RequestBody CompanyDto company) throws Exception {
        return companyService.update(token,company);
    }


    @DeleteMapping("/{companyId}")
    public void deleteCompany(@RequestHeader("Authorization") UUID token,@PathVariable("companyId") Integer companyId) throws Exception {
         companyService.delete(token,companyId);
    }


    @GetMapping("/all")
    public List<CompanyDto> getAllCompanies(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return companyService.getAll(token);
    }


    @GetMapping("/{companyId}")
    public CompanyDto getOneCompany(@RequestHeader("Authorization") UUID token,@PathVariable("companyId") int companyId) throws CouponSystemException, SQLException {
        return companyService.getSingle(token,companyId);
    }

}
