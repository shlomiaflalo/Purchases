package com.johnbryce.coupon_system_final.services.purchase;

import com.johnbryce.coupon_system_final.db.AdminTokenInternalUse;
import com.johnbryce.coupon_system_final.dto.*;
import com.johnbryce.coupon_system_final.entities.Purchase;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_final.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_final.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_final.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_final.exceptions.purchases.PurchaseException;
import com.johnbryce.coupon_system_final.log.LogCoupon;
import com.johnbryce.coupon_system_final.repositories.PurchaseRepository;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.services.company.CompanyService;
import com.johnbryce.coupon_system_final.services.coupon.CouponService;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import com.johnbryce.coupon_system_final.utils.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService {


    private final PurchaseRepository purchaseRepository;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final CouponService couponService;
    private final TokenManager tokenManager;
    private final Utils utils;
    private final AdminTokenInternalUse adminTokenInternalUse;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               @Lazy CustomerService customerService,
                               @Lazy CompanyService companyService,
                               @Lazy CouponService couponService,
                               @Lazy TokenManager tokenManager,
                               AdminTokenInternalUse adminTokenInternalUse,
                               Utils utils) {
        this.purchaseRepository = purchaseRepository;
        this.customerService = customerService;
        this.companyService = companyService;
        this.couponService = couponService;
        this.utils = utils;
        this.tokenManager = tokenManager;
        this.adminTokenInternalUse = adminTokenInternalUse;
    }


    @Transactional
    @Override
    public void purchaseCoupon(UUID token, int couponId) throws Exception {
        tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        couponTestBeforePurchase(token, couponId);
    }

    @Transactional
    @Override
    public PurchaseDto add(UUID token,PurchaseDto purchase) throws Exception {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if(purchaseRepository.existsById(purchase.getId())){
            throw new CouponSystemException(PurchaseException.PURCHASE_EXISTS);
        }

        if (companyService.isCompanyExistsByEmail(purchase.getCustomer().getEmail())){
            throw new CouponSystemException(CompanyException.COMPANY_EMAIL_ALREADY_EXISTS);
        }

        if (customerService.isCustomerEmailExists(purchase.getCustomer().getEmail()) ){
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (purchase.getCustomer().getEmail().equalsIgnoreCase("admin@admin.com")){
            throw new CouponSystemException(GenericException.EMAIL_BELONG_TO_ADMIN);
        }

        couponTestBeforePurchase
                (token,purchase.getCoupon().getId());
        return utils.convertToDto(purchaseRepository.
                findFirstByOrderByIdDesc(), PurchaseDto.class);
    }

    @Transactional
    @Override
    public PurchaseDto update(UUID token, PurchaseDto purchase) throws CouponSystemException {
         tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (!purchaseRepository.existsById(purchase.getId())) {
            throw new CouponSystemException(PurchaseException.PURCHASE_NOT_EXISTS);
        }

        if(!purchaseRepository.existsByIdAndCoupon_Company_Id(purchase.getId(),purchase.getCoupon().getCompany().getId())){
            throw new CouponSystemException(GenericException.UNAUTHORIZED_ACTION);
        }

        if (companyService.isCompanyExistsByEmail(purchase.getCustomer().getEmail())){
            throw new CouponSystemException(CompanyException.COMPANY_EMAIL_ALREADY_EXISTS);
        }

        if (customerService.isCustomerEmailExists(purchase.getCustomer().getEmail()) ){
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (purchase.getCustomer().getEmail().equalsIgnoreCase("admin@admin.com")){
            throw new CouponSystemException(GenericException.EMAIL_BELONG_TO_ADMIN);
        }

        if(isPurchaseNotChanged(purchase)){
            throw new CouponSystemException(GenericException.NO_CHANGES_HAS_BEEN_DONE);
        }

        couponTestBeforeUpdatePurchase(token,
                purchase.getCoupon().getId());

        return utils.convertToDto(purchaseRepository.
                findPurchaseByCoupon_idAndCustomer_id(
                        purchase.getCoupon().getId(),
                        purchase.getCustomer().getId()), PurchaseDto.class);

    }

    @Transactional
    @Override
    public void delete(UUID token,Integer integer) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        purchaseRepository.findById(integer)
                .orElseThrow(() -> new CouponSystemException(
                        PurchaseException.PURCHASE_NOT_EXISTS));
        purchaseRepository.deleteById(integer);

    }

    @Transactional
    @Override
    public List<PurchaseDto> getAll(UUID token) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        return utils.convertFromEntityListToDto(purchaseRepository.findAll(), PurchaseDto.class);
    }

    @Transactional
    @Override
    public PurchaseDto getSingle(UUID token,Integer integer) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        Purchase purchase = purchaseRepository.findById(integer).orElseThrow(()
                -> new CouponSystemException(PurchaseException.PURCHASE_NOT_EXISTS));

        return utils.convertToDto(purchase, PurchaseDto.class);
    }

    @Transactional
    @Override
    public boolean isExists(Integer integer) {
        return purchaseRepository.existsById(integer);
    }

    @Transactional
    @Override
    public boolean isCouponPurchasedByCustomer(int customerId, int couponId) {
        return purchaseRepository.existsPurchaseByCoupon_IdAndCustomer_Id(couponId, customerId);
    }

    @Transactional
    @Override
    public List<PurchaseDto> getCustomerPurchases(int customerId) throws CouponSystemException {
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);

        }
        return utils.convertFromEntityListToDto(
                purchaseRepository.findAllByCustomer_id(customerId),
                PurchaseDto.class);
    }


    @Transactional
    @Override
    public void deleteCouponPurchase(UUID token,int customerId, int couponId) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }
        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        Purchase purchase = Optional.ofNullable(purchaseRepository
                        .findPurchaseByCoupon_idAndCustomer_id(couponId, customerId)).
                orElseThrow(() -> new CouponSystemException(PurchaseException.PURCHASE_NOT_EXISTS));

        purchaseRepository.deleteById(purchase.getId());
    }

    @Transactional
    @Override
    public void deleteCouponPurchasesByCouponId(UUID token,int couponId) throws CouponSystemException, SQLException {
        int userId;
        try{
            userId = tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);
        }catch (Exception ignored){
            userId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);
        }

        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        if(userId!=-1){
            if (!couponService.isCouponExistsByCouponIdAndCompanyId(couponId, userId)) {
                throw new CouponSystemException(CouponException.CANNOT_EDIT_COUPON_OF_ANOTHER_COMPANY);
            }
        }

        customerService.removePurchasesByCouponId(couponId);
    }

    @Transactional
    @Override
    public void deleteCouponPurchasesByCompanyId(UUID token) throws CouponSystemException, SQLException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        customerService.removePurchasesByCompanyId(token);
    }

    @Transactional
    @Override
    public void deleteCouponPurchasesByCustomerId(UUID token,int customerId) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.ADMINISTRATOR);

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        customerService.cleanPurchaseListByCustomerId(customerId);
    }

    @Transactional
    @Override
    public void updatePurchaseViaCustomer(UUID token, PurchaseDto purchaseDto) throws CouponSystemException {
        CustomerDto customer = customerService.getSingle(token,purchaseDto.getCustomer().getId());
        List<PurchaseDto> updateListOnCustomer = new ArrayList<>();

        if (customer.getPurchases() != null && !customer.getPurchases().isEmpty()) {
            updateListOnCustomer.addAll(customer.getPurchases());
        }

        updateListOnCustomer.forEach(p -> {
            if(p.getId()==purchaseDto.getId()){
                updateListOnCustomer.set(updateListOnCustomer.indexOf(p),purchaseDto);
            }
        });

           /*8. Increasing the inventory's coupons with +1 (returning the old coupon purchase to db)
              for database only (updated by +1) - using native query*/
        couponService.updatePreviousPurchase(purchaseDto.getCoupon().getId());

              /*9. Lowering the inventory's coupons in 1
              (-1 coupon after purchasing it) and updating the
              purchased column which Not exists on dto,
              for database only (updated by +1) - using native query*/
        couponService.updateCouponAmountAndPurchased(purchaseDto.getCoupon().getId());

        customer.setPurchases(updateListOnCustomer);
        customerService.insertOrUpdateCustomerAfterLogic(customer);
    }

    @Transactional
    @Override
    public void addPurchaseViaCustomer(UUID token,PurchaseDto purchaseDto) throws CouponSystemException {
        CustomerDto customer = customerService.getSingle(adminTokenInternalUse.getAdminToken(),purchaseDto.getCustomer().getId());
        List<PurchaseDto> updateListOnCustomer = new ArrayList<>();

        if (customer.getPurchases() != null && !customer.getPurchases().isEmpty()) {
            updateListOnCustomer.addAll(customer.getPurchases());
        }

        updateListOnCustomer.add(purchaseDto);

        customer.setPurchases(updateListOnCustomer);
        customerService.insertOrUpdateCustomerAfterLogic(customer);
    }

    @Transactional
    @Override
    public void couponTestBeforeUpdatePurchase(UUID token, int couponId) throws CouponSystemException {

        int customerId = tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        //1. First Check: if coupon exists
        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        //2. 2ND Check: if customer exists
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        CouponDto couponFromDb = couponService.getSingle(token,couponId);

        //3. 3rd Check: if coupon have been purchased by customer
        if (purchaseRepository.existsPurchaseByCoupon_IdAndCustomer_Id(couponId, customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_BOUGHT_THIS_COUPON_BEFORE);
        }

        //4. Check if customer bought this coupon type before
        if (couponService.isCouponTypePurchased(customerId, couponId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_BOUGHT_THIS_COUPON_TYPE_BEFORE);
        }

        //5. Check if the coupon a customer trying to buy still exists on the inventory
        if (couponFromDb.getAmount() <= 0) {
            throw new CouponSystemException(CouponException.COUPON_IS_OUT_OF_STOCK);
        }

        //6. Checking that the due date of a coupon wasn't arrived.
        if (couponFromDb.getEndDate().isBefore(LocalDate.now())) {
            throw new CouponSystemException(CouponException.DUE_DATE_COUPON);
        }

        PurchaseDto purchaseDto = PurchaseDto.
                builder().
                customer(customerService.getSingle(adminTokenInternalUse.getAdminToken(),customerId))
                .coupon(couponFromDb)
                .build();

       /*7. Updating the purchasing via the customer entity by adding it to the customer's purchase
        list - coupon will get purchased */
        updatePurchaseViaCustomer
                (token,purchaseDto);
    }

    @Transactional
    @Override
    public void couponTestBeforePurchase(UUID token,int couponId) throws CouponSystemException {

        int customerId = tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        //1. First Check: if coupon exists
        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        //2. 2ND Check: if customer exists
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        CouponDto couponFromDb = couponService.getSingle(token,couponId);

        //3. 3rd Check: if coupon have been purchased by customer
        if (purchaseRepository.existsPurchaseByCoupon_IdAndCustomer_Id(couponId, customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_BOUGHT_THIS_COUPON_BEFORE);
        }

        //4. Check if customer bought this coupon type before
        if (couponService.isCouponTypePurchased(customerId, couponId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_BOUGHT_THIS_COUPON_TYPE_BEFORE);
        }

        //5. Check if the coupon a customer trying to buy still exists on the inventory
        if (couponFromDb.getAmount() <= 0) {
            throw new CouponSystemException(CouponException.COUPON_IS_OUT_OF_STOCK);
        }

        //6. Checking that the due date of a coupon wasn't arrived.
        if (couponFromDb.getEndDate().isBefore(LocalDate.now())) {
            throw new CouponSystemException(CouponException.DUE_DATE_COUPON);
        }

        PurchaseDto purchaseDto = PurchaseDto.
                    builder().
                    customer(customerService.getSingle(adminTokenInternalUse.getAdminToken(),customerId))
                    .coupon(couponFromDb)
                    .build();

      /*7. Adding the purchasing via the customer entity by adding it to the customer's purchase
        list - coupon will get purchased */
        addPurchaseViaCustomer
                (token,purchaseDto);

              /*8. Lowering the inventory's coupons in 1
              (-1 coupon after purchasing it) and updating the
              purchased column which Not exists on dto,
              for database only (updated by +1) - using native query*/
        couponService.updateCouponAmountAndPurchased(couponId);
    }

    @Override
    public boolean isPurchaseNotChanged(PurchaseDto purchaseToUpdate) throws CouponSystemException {

        //Updatable Coupon details
        String currentCompany = purchaseToUpdate.getCoupon().getCompany().getEmail();
        String currentCategory = purchaseToUpdate.getCoupon().getCategory().toString();
        String currentTitle = purchaseToUpdate.getCoupon().getTitle();
        String currentDescription = purchaseToUpdate.getCoupon().getDescription();
        LocalDate currentStartDate = purchaseToUpdate.getCoupon().getStartDate();
        LocalDate currentEndDate = purchaseToUpdate.getCoupon().getEndDate();
        int currentAmount = purchaseToUpdate.getCoupon().getAmount();
        double currentPrice = purchaseToUpdate.getCoupon().getPrice();
        String currentImage= purchaseToUpdate.getCoupon().getImage();

        //Current Coupon details
        PurchaseDto purchaseFromDb =utils.convertToDto(purchaseRepository
                .getReferenceById(purchaseToUpdate.getId()), PurchaseDto.class);

        String previousCompany = purchaseFromDb.getCoupon().getCompany().getEmail();
        String previousCategory = purchaseFromDb.getCoupon().getCategory().toString();
        String previousTitle = purchaseFromDb.getCoupon().getTitle();
        String previousDescription = purchaseFromDb.getCoupon().getDescription();
        LocalDate previousStartDate = purchaseFromDb.getCoupon().getStartDate();
        LocalDate previousEndDate = purchaseFromDb.getCoupon().getEndDate();
        int previousAmount = purchaseFromDb.getCoupon().getAmount();
        double previousPrice = purchaseFromDb.getCoupon().getPrice();
        String previousImage= purchaseFromDb.getCoupon().getImage();

         //Updatable Customer details
         String currentFirstName = purchaseToUpdate.getCustomer().getFirstName();
         String currentLastName = purchaseToUpdate.getCustomer().getLastName();
         String currentEmail = purchaseToUpdate.getCustomer().getEmail();
         String currentPassword = purchaseToUpdate.getCustomer().getPassword();

        //Current Customer details
        String previousFirstName = purchaseFromDb.getCustomer().getFirstName();
        String previousLastName = purchaseFromDb.getCustomer().getLastName();
        String previousEmail = purchaseFromDb.getCustomer().getEmail();
        String previousPassword = purchaseFromDb.getCustomer().getPassword();

        boolean isCouponNotChanged = currentCompany.equals(previousCompany) && currentCategory.equals(previousCategory)
                && currentTitle.equals(previousTitle) && currentDescription.equals(previousDescription)
                && currentStartDate.equals(previousStartDate) && currentEndDate.equals(previousEndDate)
                && currentAmount == previousAmount && currentPrice == previousPrice && currentImage.equals(previousImage);

        boolean isCustomerNotChanged = currentFirstName.equals(previousFirstName) && currentLastName.equals(previousLastName)
                && currentEmail.equals(previousEmail) && currentPassword.equals(previousPassword);

        return isCouponNotChanged && isCustomerNotChanged;
        }
    }

