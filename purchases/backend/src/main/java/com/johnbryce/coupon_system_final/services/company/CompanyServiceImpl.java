package com.johnbryce.coupon_system_final.services.company;

import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.entities.Company;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_final.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_final.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_final.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_final.repositories.CompanyRepository;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.LoginResponse;
import com.johnbryce.coupon_system_final.security.TokenInformation;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.services.coupon.CouponService;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import com.johnbryce.coupon_system_final.utils.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CouponService couponService;
    private final Utils utils;
    private final TokenManager tokenManager;
    private final CustomerService customerService;


    public CompanyServiceImpl(CompanyRepository companyRepository,
                              @Lazy CouponService couponService,
                              @Lazy TokenManager tokenManager,
                              Utils utils,  @Lazy CustomerService customerService) {
        this.companyRepository = companyRepository;
        this.couponService = couponService;
        this.utils = utils;
        this.tokenManager = tokenManager;
        this.customerService = customerService;
    }

    @Override
    public LoginResponse login(String email, String password) throws CouponSystemException {
        if (!companyRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(GenericException.EMAIL_AND_PASSWORD_IS_NOT_CORRECT);
        }

        CompanyDto companyByEmail = utils.convertToDto(
                companyRepository.findByEmail(email), CompanyDto.class);

        TokenInformation tokenInformation = new TokenInformation();
        tokenInformation.setId(companyByEmail.getId());
        tokenInformation.setEmail(email);
        tokenInformation.setName(companyByEmail.getName());
        tokenInformation.setClientType(ClientType.COMPANY);

        UUID newTokenResponse = tokenManager.addToken(tokenInformation);
        return LoginResponse.builder().id(companyByEmail.getId()).token(newTokenResponse).
                email(email).name(companyByEmail.getName()).clientType(ClientType.COMPANY).build();
    }

    @Override
    public CompanyDto getCompany(int id) throws CouponSystemException {
        Company company = companyRepository.findById(id).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));
        return utils.convertToDto(
                company, CompanyDto.class);
    }

    @Override
    public void saveCompany(CompanyDto companyDto) throws CouponSystemException {
         companyRepository.findById(companyDto.getId()).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));
        Company company = utils.convertFromDto(companyDto, Company.class);
        companyRepository.save(company);
    }

    @Override
    public boolean isCompanyLogin(String email){
        return companyRepository.existsByEmail(email);
    }

    @Override
    public CompanyDto getRandomCompany() throws CouponSystemException {
        CompanyDto companyDto =
                utils.convertToDto(companyRepository.findRandomCompany(), CompanyDto.class);
        if (companyDto == null) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        return companyDto;
    }

    @Override
    public CompanyDto getFirstCompanyRecord() throws CouponSystemException {
        return utils.convertToDto(companyRepository.findFirstByOrderByIdAsc(), CompanyDto.class);
    }

    @Override
    public CompanyDto getLastCompanyRecord() throws CouponSystemException {
        return utils.convertToDto(companyRepository.findFirstByOrderByIdDesc(), CompanyDto.class);

    }

    @Override
    public boolean isCompanyEmailExistsExclude(String email, int companyId) {
        return companyRepository.
                existsByEmailAndIdNot(email, companyId);
    }

    @Override
    public boolean isCompanyExistsByEmailAndPassword(String email, String password) {
        return companyRepository.existsByEmailAndPassword(email, password);
    }

    @Override
    public boolean isCompanyExistsByName(String name) {
        return companyRepository.existsByName(name);
    }

    @Override
    public boolean isCompanyExistsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }

    @Override
    public boolean isCompanyExistsByNameAndId(String name, int id) {
        return companyRepository.existsByNameAndId(name, id);
    }

    @Transactional
    @Override
    public CompanyDto add(UUID token,CompanyDto companyDto) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (companyRepository.existsByName(companyDto.getName())) {
            throw new CouponSystemException(CompanyException.COMPANY_NAME_ALREADY_EXISTS);
        }
        if (companyRepository.existsByEmail(companyDto.getEmail())){
            throw new CouponSystemException(CompanyException.COMPANY_EMAIL_ALREADY_EXISTS);
        }

        if (customerService.isCustomerEmailExists(companyDto.getEmail()) ){
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (companyDto.getEmail().equalsIgnoreCase("admin@admin.com")){
            throw new CouponSystemException(GenericException.EMAIL_BELONG_TO_ADMIN);
        }

        if (companyRepository.existsById(companyDto.getId())) {
            throw new CouponSystemException(CompanyException.COMPANY_ID_ALREADY_EXISTS);
        }

        Company company = utils.convertFromDto(companyDto, Company.class);
        Company companyInDb = companyRepository.save(company);
        return utils.convertToDto(companyInDb, CompanyDto.class);
    }

    @Transactional
    @Override
    public CompanyDto update(UUID token,CompanyDto company) throws CouponSystemException {
        try {
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        } catch (CouponSystemException e) {
            // If not admin, try as company
            tokenManager.getUserIdFromToken(token, ClientType.COMPANY);
            //Only admin can change company name - company can't
            if (!companyRepository.existsByNameAndId(company.getName(), company.getId())) {
                throw new CouponSystemException(CompanyException.CANNOT_EDIT_COMPANY_NAME_AND_ID);
            }
        }

        Company companyFromDb = companyRepository.findById(company.getId()).
                orElseThrow(() ->
                        new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        //If there is any company with this email except myself - because i can update my
        //email if no other company is using it.
        if (companyRepository.existsByEmailAndIdNot(company.getEmail(), company.getId())){
            throw new CouponSystemException(CompanyException.COMPANY_EMAIL_ALREADY_EXISTS);
        }

        if (customerService.isCustomerEmailExists(company.getEmail())){
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (company.getEmail().equalsIgnoreCase("admin@admin.com")){
            throw new CouponSystemException(GenericException.EMAIL_BELONG_TO_ADMIN);
        }

        if(isCompanyNotChanged(company)){
            throw new CouponSystemException(GenericException.NO_CHANGES_HAS_BEEN_DONE);
        }

        companyFromDb.setName(company.getName());
        companyFromDb.setEmail(company.getEmail());
        companyFromDb.setPassword(company.getPassword());

        return utils.convertToDto(companyFromDb, CompanyDto.class);
    }

    @Transactional
    @Override
    public void delete(UUID token,Integer integer) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        companyRepository.findById(integer).
                orElseThrow(() ->
                        new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        companyRepository.deleteById(integer);
    }

    @Override
    public List<CompanyDto> getAll(UUID token) throws CouponSystemException {
        try{
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        }catch (CouponSystemException ignored){
        }
        try{
            tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        }catch (CouponSystemException ignored){
        }

        return utils.convertFromEntityListToDto(companyRepository.findAll(), CompanyDto.class);
    }

    @Override
    public CompanyDto getSingle(UUID token,Integer integer) throws CouponSystemException {
        try {
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        } catch (CouponSystemException e) {
            // If not admin, try as company
            tokenManager.getUserIdFromToken(token, ClientType.COMPANY);
        }

        Company company = companyRepository.findById(integer).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        return utils.convertToDto(company, CompanyDto.class);
    }

    @Override
    public CompanyDto getSingleInternal(int companyId) throws CouponSystemException {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));
        return utils.convertToDto(company, CompanyDto.class);
    }

    @Override
    public boolean isExists(Integer integer) {
        return companyRepository.existsById(integer);
    }

    @Transactional
    @Override
    public void updateCompanyCouponsCountMinus(int companyId) throws CouponSystemException {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        company.setCouponCount(company.getCouponCount() - 1);
    }

    @Transactional
    @Override
    public void updateCompanyCouponsCountPlus(int companyId) throws CouponSystemException {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        company.setCouponCount(company.getCouponCount() + 1);
    }

    @Transactional
    @Override
    public void deleteCouponByCompanyId(UUID token, int couponId) throws CouponSystemException {
        int companyId= tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        for (int i = 0; i < company.getCoupons().size(); i++) {
            if (company.getCoupons().get(i).getId() == couponId) {
                company.getCoupons().remove(i);
                return;
            }
        }
    }


    @Override
    public CompanyDto getSingleByEmail(UUID token,String email) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (!companyRepository.existsByEmail(email)) {
            throw new CouponSystemException(GenericException.EMAIL_IS_NOT_FOUND);
        }

        return utils.convertToDto(companyRepository.findByEmail(email), CompanyDto.class);
    }
    @Override
    public boolean isCompanyNotChanged(CompanyDto companyDto) throws CouponSystemException {

        //Updatable Company details
        String currentName = companyDto.getName();
        String currentEmail = companyDto.getEmail();
        String currentPassword = companyDto.getPassword();

        //Current Company details
        CompanyDto companyFromDb = utils.convertToDto(
                companyRepository.getReferenceById(companyDto.getId()), CompanyDto.class);

        String previousName = companyFromDb.getName();
        String previousEmail = companyFromDb.getEmail();
        String previousPassword = companyFromDb.getPassword();

        return currentName.equals(previousName) && currentEmail.equals(previousEmail)
                && currentPassword.equals(previousPassword);
    }

}
