package com.johnbryce.coupon_system_final.services.customer;

import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.dto.PurchaseDto;
import com.johnbryce.coupon_system_final.entities.Customer;
import com.johnbryce.coupon_system_final.entities.Purchase;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.exceptions.categories.CategoryException;
import com.johnbryce.coupon_system_final.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_final.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_final.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_final.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_final.exceptions.purchases.PurchaseException;
import com.johnbryce.coupon_system_final.repositories.CustomerRepository;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.LoginResponse;
import com.johnbryce.coupon_system_final.security.TokenInformation;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.services.category.CategoryService;
import com.johnbryce.coupon_system_final.services.company.CompanyService;
import com.johnbryce.coupon_system_final.services.coupon.CouponService;
import com.johnbryce.coupon_system_final.services.purchase.PurchaseService;
import com.johnbryce.coupon_system_final.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PurchaseService purchaseService;
    private final Utils utils;
    private final CategoryService categoryService;
    private final CouponService couponService;
    private final CompanyService companyService;
    private final TokenManager tokenManager;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            @Lazy PurchaseService purchaseService,
            Utils utils,
            CategoryService categoryService,
            @Lazy CouponService couponService,
            @Lazy CompanyService companyService
            ,@Lazy TokenManager tokenManager) {
        this.customerRepository = customerRepository;
        this.purchaseService = purchaseService;
        this.utils = utils;
        this.categoryService = categoryService;
        this.couponService = couponService;
        this.companyService = companyService;
        this.tokenManager = tokenManager;

    }

    @Transactional
    @Override
    public LoginResponse login(String email, String password) throws CouponSystemException {
        if (!customerRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(GenericException.EMAIL_AND_PASSWORD_IS_NOT_CORRECT);
        }

        CustomerDto customerByEmail = utils.convertToDto(
                customerRepository.findByEmail(email), CustomerDto.class);

        TokenInformation tokenInformation = new TokenInformation();
        tokenInformation.setId(customerByEmail.getId());
        tokenInformation.setEmail(email);
        tokenInformation.setName(customerByEmail.getFirstName()+" "+customerByEmail.getLastName());
        tokenInformation.setClientType(ClientType.CUSTOMER);

        UUID newTokenResponse = tokenManager.addToken(tokenInformation);
        return LoginResponse.builder().id(customerByEmail.getId()).token(newTokenResponse).
                email(email).name(customerByEmail.getFirstName()+" "+customerByEmail.getLastName()).
                clientType(ClientType.CUSTOMER).build();
    }

    @Transactional
    @Override
    public void cleanPurchaseListByCustomerId(int customerId) throws CouponSystemException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new CouponSystemException(
                        CustomerException.CUSTOMER_IS_NOT_EXISTS));
        if (customer.getPurchases() != null && !customer.getPurchases().isEmpty()) {
            customer.getPurchases().clear();
        }
        customerRepository.save(customer);
    }

    @Transactional
    @Override
    public CustomerDto add(UUID token,CustomerDto customer) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (customerRepository.existsById(customer.getId())) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_ALREADY_EXISTS);
        }

        if (customerRepository.existsByEmail(customer.getEmail())){
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (companyService.isCompanyExistsByEmail(customer.getEmail())){
            throw new CouponSystemException(CompanyException.COMPANY_EMAIL_ALREADY_EXISTS);
        }

        if (customer.getEmail().equalsIgnoreCase("admin@admin.com")){
            throw new CouponSystemException(GenericException.EMAIL_BELONG_TO_ADMIN);
        }

        if (customerRepository.existsByFirstNameAndLastName(customer.getFirstName(), customer.getLastName())) {
            throw new CouponSystemException(CustomerException.FIRST_AND_LAST_NAME_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        Customer customerConvert = utils.convertFromDto(customer, Customer.class);
        Customer customerInDb = customerRepository.save(customerConvert);
        return utils.convertToDto(customerInDb, CustomerDto.class);
    }

    @Transactional
    @Override
    public void insertOrUpdateCustomerAfterLogic(CustomerDto customer) throws CouponSystemException {
        customerRepository.save(utils.convertFromDto(customer, Customer.class));
    }

    @Transactional
    @Override
    public CustomerDto update(UUID token,CustomerDto customerDto) throws CouponSystemException {
        try {
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        } catch (CouponSystemException e) {
            // If not admin, try as customer
            tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        }

        Customer existingCustomer =
                customerRepository.findById(customerDto.getId()).orElseThrow(() ->
                        new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS));

        // Check for email conflict
        if (customerRepository.existsByEmailAndIdNot(customerDto.getEmail(), customerDto.getId())) {
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (companyService.isCompanyExistsByEmail(customerDto.getEmail())){
            throw new CouponSystemException(CompanyException.COMPANY_EMAIL_ALREADY_EXISTS);
        }

        if (customerDto.getEmail().equalsIgnoreCase("admin@admin.com")){
            throw new CouponSystemException(GenericException.EMAIL_BELONG_TO_ADMIN);
        }

        // Check for name conflict
        if (customerRepository.existsByFirstNameAndLastNameAndIdNot
                (customerDto.getFirstName(), customerDto.getLastName(), customerDto.getId())) {
            throw new CouponSystemException(CustomerException.
                    FIRST_AND_LAST_NAME_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if(isCustomerNotChanged(customerDto)){
            throw new CouponSystemException(GenericException.NO_CHANGES_HAS_BEEN_DONE);
        }

        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPassword(customerDto.getPassword());

            if (customerDto.getPurchases() != null &&
                    !customerDto.getPurchases().isEmpty()) {
                // Add new purchases to the existing ones
                existingCustomer.getPurchases().addAll(
                        utils.convertFromDtoToEntityList(
                                customerDto.getPurchases(), Purchase.class));
            }

        return utils.convertToDto(existingCustomer, CustomerDto.class);
    }


    @Transactional
    @Override
    public void delete(UUID token,Integer integer) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        customerRepository.findById(integer)
                .orElseThrow(() -> new CouponSystemException(
                        CustomerException.CUSTOMER_IS_NOT_EXISTS));

        cleanPurchaseListByCustomerId(integer);
        customerRepository.deleteById(integer);
    }

    @Transactional
    @Override
    public List<CustomerDto> getAll(UUID token) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        return utils.convertFromEntityListToDto(customerRepository.findAll(), CustomerDto.class);
    }

    @Transactional
    @Override
    public CustomerDto getSingle(UUID token,Integer integer) throws CouponSystemException {
        try {
            tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        } catch (CouponSystemException e) {
            // If not admin, try as customer
            tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        }
        Customer customer = customerRepository.findById(integer).orElseThrow(() ->
                new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS));
        return utils.convertToDto(customer, CustomerDto.class);
    }

    @Transactional
    @Override
    public boolean isExists(Integer id) {
        return customerRepository.existsById(id);
    }

    @Transactional
    @Override
    public CustomerDto getFirstCustomerRecord() throws CouponSystemException {
        return utils.convertToDto(customerRepository.findFirstByOrderByIdAsc(), CustomerDto.class);
    }

    @Transactional
    @Override
    public CustomerDto getLastCustomerRecord() throws CouponSystemException {
        return utils.convertToDto(customerRepository.findFirstByOrderByIdDesc(), CustomerDto.class);
    }

    @Transactional
    @Override
    public List<CustomerDto> getAllCustomersByCouponCategory(UUID token,int categoryId) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS);
        }

       return
                purchaseService.getAll(token).stream().filter((coupon) ->
                        coupon.getCoupon().getCategory().getId() == categoryId).map(
                        PurchaseDto::getCustomer).toList();
    }

    @Transactional
    @Override
    public boolean isCustomerEmailExistsExclude(String email, int customerId) {
      return customerRepository.existsByEmailAndIdNot(email, customerId);
    }

    @Transactional
    @Override
    public boolean isCustomerEmailExistsById(String email, int customerId) {
        return customerRepository.existsByEmailAndId(email, customerId);
    }

    @Transactional
    @Override
    public boolean isCustomerEmailExists(String email) {
       return customerRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public boolean isCustomerExists(String email, String password) {
       return customerRepository.existsByEmailAndPassword(email, password);
    }


    @Transactional
    @Override
    public CustomerDto getSingleByEmail(UUID token,String email) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (!customerRepository.existsByEmail(email)) {
            throw new CouponSystemException(GenericException.EMAIL_IS_NOT_FOUND);
        }

        return utils.convertToDto(customerRepository.findByEmail(email), CustomerDto.class);
    }

    @Transactional
    @Override
    public void removePurchasesByCouponId(int couponId) throws CouponSystemException {

        final boolean[] isGotPurchases = {false};

        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        List<Customer> customers = customerRepository.findAll();

        customers.forEach(customer -> {
            boolean isRemoved = customer.getPurchases().removeIf((
                    purchase) -> purchase.getCoupon().getId()
                    == couponId);

            if (isRemoved) {
                customerRepository.saveAndFlush(customer);
            }
            if (isRemoved && !isGotPurchases[0]) {
                isGotPurchases[0] = true;
            }
        });
        if(!isGotPurchases[0]){
            throw new CouponSystemException(PurchaseException.PURCHASES_NOT_EXISTS_FOR_THIS_COUPON_ID);
        }
    }

    @Transactional
    @Override
    public void removePurchasesByCompanyId(UUID token) throws CouponSystemException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        List<Customer> customers = customerRepository.findAll();

        customers.forEach(customer -> {
            boolean isRemoved = customer.getPurchases().removeIf((
                    purchase) -> purchase.getCoupon().getCompany().getId()
                    == companyId);

            if (isRemoved) {
                customerRepository.saveAndFlush(customer);
            }
        });
    }
    @Override
    public boolean isCustomerNotChanged(CustomerDto customerDto) throws CouponSystemException {

        //Updatable Customer details
        String currentFirstName = customerDto.getFirstName();
        String currentLastName = customerDto.getLastName();
        String currentEmail = customerDto.getEmail();
        String currentPassword = customerDto.getPassword();

        //Current Customer details
        CustomerDto customerFromDb = utils.convertToDto(
                customerRepository.getReferenceById(customerDto.getId()), CustomerDto.class);

        String previousFirstName = customerFromDb.getFirstName();
        String previousLastName = customerFromDb.getLastName();
        String previousEmail = customerFromDb.getEmail();
        String previousPassword = customerFromDb.getPassword();

        return currentFirstName.equals(previousFirstName) && currentLastName.equals(previousLastName)
                && currentEmail.equals(previousEmail) && currentPassword.equals(previousPassword);
    }

}
