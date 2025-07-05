package com.johnbryce.coupon_system_final.services.coupon;

import com.johnbryce.coupon_system_final.db.AdminTokenInternalUse;
import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.dto.CouponDto;
import com.johnbryce.coupon_system_final.dto.PurchaseDto;
import com.johnbryce.coupon_system_final.entities.Coupon;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.exceptions.categories.CategoryException;
import com.johnbryce.coupon_system_final.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_final.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_final.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_final.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_final.repositories.CouponRepository;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.services.category.CategoryService;
import com.johnbryce.coupon_system_final.services.company.CompanyService;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import com.johnbryce.coupon_system_final.services.purchase.PurchaseService;
import com.johnbryce.coupon_system_final.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final PurchaseService purchaseService;
    private final CategoryService categoryService;
    private final TokenManager tokenManager;
    private final Utils utils;
    private final AdminTokenInternalUse adminTokenInternalUse;

    public CouponServiceImpl(CouponRepository couponRepository,
                             @Lazy CustomerService customerService,
                             @Lazy CompanyService companyService,
                             @Lazy PurchaseService purchaseService,
                             @Lazy CategoryService categoryService,
                             @Lazy TokenManager tokenManager,
                             Utils utils, AdminTokenInternalUse adminTokenInternalUse) {
        this.couponRepository = couponRepository;
        this.customerService = customerService;
        this.companyService = companyService;
        this.purchaseService = purchaseService;
        this.categoryService = categoryService;
        this.utils = utils;
        this.tokenManager = tokenManager;
        this.adminTokenInternalUse = adminTokenInternalUse;
    }

    @Transactional
    @Override
    public void updateCouponAmountAndPurchased(int couponId) {
        couponRepository.updateCouponAmountAndPurchased(couponId);
    }

    @Transactional
    @Override
    public void updatePreviousPurchase(int couponId) {
        couponRepository.updatePreviousPurchase(couponId);
    }

    @Transactional
    @Override
    public List<CouponDto> getCompanyCouponsByCompany(UUID token) throws CouponSystemException {
       int companyId =  tokenManager.getUserIdFromToken(token, ClientType.COMPANY);
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        return utils.convertFromEntityListToDto
                (couponRepository.findAllByCompany_Id(companyId), CouponDto.class);
    }

    @Transactional
    @Override
    public List<CouponDto> getCompanyCouponsByCustomer(UUID token,int companyId) throws CouponSystemException {
           tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        return utils.convertFromEntityListToDto
                (couponRepository.findAllByCompany_Id(companyId), CouponDto.class);
    }


    @Transactional
    @Override
    public void removeAllExpiredCoupons() {
        couponRepository.removeAllExpiredCoupons();
    }

    @Override
    public CouponDto getLastRecordCoupon() throws CouponSystemException {
        return utils.convertToDto
                (couponRepository.
                                findFirstByOrderByIdDesc(),
                        CouponDto.class);
    }

    @Override
    public CouponDto getFirstRecordCoupon() throws CouponSystemException {
        return utils.convertToDto
                (couponRepository.findFirstByOrderByIdAsc(),
                        CouponDto.class);
    }


    @Override
    public boolean isCouponAvailable(int couponId) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() ->
                new CouponSystemException(CouponException.COUPON_NOT_EXISTS));
        return coupon.getAmount() > 0;
    }

    @Override
    public boolean dueDateCouponCheck(int couponId) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() ->
                new CouponSystemException(CouponException.COUPON_NOT_EXISTS));
        return coupon.getEndDate().isAfter(LocalDate.now());
    }

    @Override
    public boolean isCouponTitleExistsByCompanyId(int companyId, String title) {
       return couponRepository.existsByCompanyIdAndTitle(companyId, title);
    }

    @Override
    public boolean isCouponExistsByCouponIdAndCompanyId(int couponId, int companyId) {
        return couponRepository.existsByIdAndCompany_Id(couponId, companyId);
    }

    @Override
    public boolean isCouponTitleExistsByCompanyIdExclude(int companyId, String title, int CouponId) {
       return couponRepository.existsByCompany_idAndTitleAndIdNot(companyId, title, CouponId);
    }

    @Override
    public List<CouponDto> getCustomerCoupons(UUID token) throws CouponSystemException {
        int customerId = tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        return purchaseService.getCustomerPurchases(customerId).stream().map(PurchaseDto::getCoupon).toList();
    }


    @Override
    public List<CouponDto> getCustomerCouponsByCategoryId(UUID token, int categoryId) throws CouponSystemException {
        int customerId = tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }
        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS_BY_ID);
        }

        return purchaseService.getCustomerPurchases(customerId).stream()
                .map(PurchaseDto::getCoupon).filter(coupon -> coupon.getCategory().getId() == categoryId).toList();
    }

    @Override
    public List<CouponDto> getCustomerCouponsByMaxPrice(UUID token, double maxPrice) throws CouponSystemException {
        int customerId  = tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }
        if (maxPrice <= 0 || maxPrice > 1_000_000) {
            throw new CouponSystemException(CouponException.COUPON_MAX_PRICE_IS_OUT_OF_RANGE);
        }

        return purchaseService.getCustomerPurchases(customerId).stream()
                .map(PurchaseDto::getCoupon).filter(coupon -> coupon.getPrice() <= maxPrice).toList();
    }

    @Transactional
    @Override
    public CouponDto add(UUID token,CouponDto coupon) throws CouponSystemException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (couponRepository.existsById(coupon.getId())) {
            throw new CouponSystemException(CouponException.COUPON_ALREADY_EXISTS);
        }

        if (couponRepository.existsByTitleAndDescription(coupon.getTitle(), coupon.getDescription())) {
            throw new CouponSystemException(CouponException.COUPON_EXISTS_BY_TITLE_AND_DESCRIPTION);
        }

        companyService.updateCompanyCouponsCountPlus(companyId);

        if(coupon.getCompany()==null){
            coupon.setCompany(companyService.getCompany(companyId));
        }

        Coupon couponAdded = utils.convertFromDto(coupon, Coupon.class);
        Coupon couponInDb = couponRepository.save(couponAdded);
        return utils.convertToDto(couponInDb, CouponDto.class);
    }

    @Transactional
    @Override
    public CouponDto update(UUID token,CouponDto coupon) throws CouponSystemException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        CouponDto couponDto = utils.convertToDto(
                couponRepository.findById(coupon.getId()).orElseThrow(
                        () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS)),
                CouponDto.class);

        if (!couponRepository.existsByIdAndCompany_Id(couponDto.getId(), companyId)) {
            throw new CouponSystemException(CouponException.CANNOT_EDIT_COUPON_OF_ANOTHER_COMPANY);
        }

        if (couponRepository.existsByTitleAndDescriptionAndIdNot
                (coupon.getTitle(), coupon.getDescription(), coupon.getId())) {
            throw new CouponSystemException(CouponException.COUPON_EXISTS_BY_TITLE_AND_DESCRIPTION);
        }

        if(isCouponNotChanged(couponDto)){
            throw new CouponSystemException(GenericException.NO_CHANGES_HAS_BEEN_DONE);
        }

        if(coupon.getCompany()==null){
            coupon.setCompany(companyService.getCompany(companyId));
        }

        Coupon couponUpdated = utils.convertFromDto(coupon, Coupon.class);
        Coupon couponInDb = couponRepository.save(couponUpdated);

        return utils.convertToDto(couponInDb, CouponDto.class);
    }

    @Transactional
    @Override
    public void delete(UUID token,Integer integer) throws CouponSystemException {
        int companyId  = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!couponRepository.existsByIdAndCompany_Id(integer, companyId)) {
            throw new CouponSystemException(CouponException.CANNOT_EDIT_COUPON_OF_ANOTHER_COMPANY);
        }

        couponRepository.findById(integer).orElseThrow(
                () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS));

        companyService.updateCompanyCouponsCountMinus(companyId);

        couponRepository.deleteById(integer);
    }

    @Override
    public List<CouponDto> getAll(UUID token) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);
        return utils.convertFromEntityListToDto(couponRepository.findAll(), CouponDto.class);
    }

    @Override
    public CouponDto getSingle(UUID token,Integer integer) throws CouponSystemException {
        tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        Coupon coupon = couponRepository.findById(integer).
                orElseThrow(
                        () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS));
        return utils.convertToDto(coupon, CouponDto.class);
    }

    @Override
    public boolean isExists(Integer integer) {
        return couponRepository.existsById(integer);
    }

    @Override
    public List<CouponDto> getCompanyCouponsByCompany(UUID token, double maxPrice) throws CouponSystemException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (maxPrice <= 0 || maxPrice > 1_000_000) {
            throw new CouponSystemException(CouponException.COUPON_MAX_PRICE_IS_OUT_OF_RANGE);
        }


        return utils.convertFromEntityListToDto(couponRepository.
                findAllByCompanyIdAndPriceIsLessThanEqual(companyId, maxPrice), CouponDto.class);
    }

    @Override
    public List<CouponDto> getCompanyCouponsByCustomer(UUID token, double maxPrice,int companyId) throws CouponSystemException {
         tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (maxPrice <= 0 || maxPrice > 1_000_000) {
            throw new CouponSystemException(CouponException.COUPON_MAX_PRICE_IS_OUT_OF_RANGE);
        }


        return utils.convertFromEntityListToDto(couponRepository.
                findAllByCompanyIdAndPriceIsLessThanEqual(companyId, maxPrice), CouponDto.class);
    }

    @Transactional
    @Override
    public CouponDto addCouponByCompany(UUID token, CouponDto coupon) throws CouponSystemException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (couponRepository.existsById(coupon.getId())) {
            throw new CouponSystemException(CouponException.COUPON_ALREADY_EXISTS);
        }
        if (couponRepository.existsByCompanyIdAndTitle(companyId, coupon.getTitle())) {
            throw new CouponSystemException(CouponException.COUPON_TITLE_EXISTS_FOR_THIS_COMPANY);
        }

        if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
            throw new CouponSystemException(GenericException.STARTING_DATE_MUST_BE_BEFORE_ITS_END);
        }

        if(coupon.getCompany()==null){
            coupon.setCompany(companyService.getCompany(companyId));
        }

        if (coupon.getCategory() == null) {
            coupon.setCategory(categoryService.getRandomCategory());
        }

        companyService.updateCompanyCouponsCountPlus(companyId);
        Coupon couponInDb = couponRepository.save(utils.convertFromDto(coupon, Coupon.class));

        return utils.convertToDto(couponInDb, CouponDto.class);
    }

    @Transactional
    @Override
    public CouponDto updateCouponByCompany(UUID token, CouponDto coupon) throws CouponSystemException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        CouponDto couponDto = utils.convertToDto(couponRepository.findById(coupon.getId()).orElseThrow(
                () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS)
        ), CouponDto.class);

        if(isCouponNotChanged(coupon)){
            throw new CouponSystemException(GenericException.NO_CHANGES_HAS_BEEN_DONE);
        }

        if (!couponRepository.existsByIdAndCompany_Id(coupon.getId(), companyId)) {
            throw new CouponSystemException(CouponException.CANNOT_EDIT_COUPON_OF_ANOTHER_COMPANY);
        }

        /*In case the client wants to update coupon with title that exists for this company
        According to the instructions, we cannot add a new coupon with duplicate title*/
        if (couponRepository.existsByCompany_idAndTitleAndIdNot(companyId,
                coupon.getTitle(), coupon.getId())) {
            throw new CouponSystemException(CouponException.COUPON_TITLE_EXISTS_FOR_THIS_COMPANY);
        }

        if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
            throw new CouponSystemException(GenericException.STARTING_DATE_MUST_BE_BEFORE_ITS_END);
        }

        if(coupon.getCompany()==null){
            coupon.setCompany(companyService.getCompany(companyId));
        }
        if (coupon.getCategory() == null) {
            coupon.setCategory(couponDto.getCategory());
        }
        Coupon couponInDb = couponRepository.save(utils.convertFromDto(coupon, Coupon.class));

        return utils.convertToDto(couponInDb, CouponDto.class);
    }

    @Transactional
    @Override
    public void deleteCouponByCompany(UUID token, int couponId) throws CouponSystemException {
        int companyId = tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() ->
                new CouponSystemException(CouponException.COUPON_NOT_EXISTS));

        companyService.updateCompanyCouponsCountMinus(companyId);

        companyService.deleteCouponByCompanyId(token, coupon.getId());
    }

    @Override
    public List<CouponDto> getCompanyCouponsByCategoryIdAndByCompany(UUID token, int categoryId) throws CouponSystemException {
        int companyId =  tokenManager.getUserIdFromToken(token, ClientType.COMPANY);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS_BY_ID);
        }

        return utils.convertFromEntityListToDto(couponRepository.
                findAllByCompany_IdAndCategory_Id(
                        companyId, categoryId), CouponDto.class);
    }

    @Override
    public List<CouponDto> getCompanyCouponsByCategoryIdAndByCustomer(UUID token, int categoryId,int companyId) throws CouponSystemException {
         tokenManager.getUserIdFromToken(token, ClientType.CUSTOMER);

        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS_BY_ID);
        }

        return utils.convertFromEntityListToDto(couponRepository.
                findAllByCompany_IdAndCategory_Id(
                        companyId, categoryId), CouponDto.class);
    }


    @Override
    public boolean isCouponTypePurchased(int customerId, int couponId) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(couponId).
                orElseThrow(() -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS));

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        CouponDto couponDto = utils.convertFromDto(coupon, CouponDto.class);

        boolean couponCategoryFoundOnCustomerPurchases = false;
        boolean isNotHaveAnyPurchaseHistory = true;
        try {
            if (!purchaseService.getCustomerPurchases(customerId).isEmpty()) {
                isNotHaveAnyPurchaseHistory = false;
            }
        } catch (CouponSystemException e) {
            log.error("", e);
        }

        if (!isNotHaveAnyPurchaseHistory) {
            try {
                couponCategoryFoundOnCustomerPurchases =
                        purchaseService.getCustomerPurchases(customerId).stream().
                                map(PurchaseDto::getCoupon).anyMatch(c ->
                                        c.getCategory().equals(couponDto.getCategory()));
            } catch (CouponSystemException e) {
                log.error("", e);
            }
        }

        return couponCategoryFoundOnCustomerPurchases;
    }
    @Override
    public boolean isCouponNotChanged(CouponDto couponDto) throws CouponSystemException {

        //Updatable Coupon details
        String currentCompany = couponDto.getCompany().getEmail();
        String currentCategory = couponDto.getCategory().toString();
        String currentTitle = couponDto.getTitle();
        String currentDescription = couponDto.getDescription();
        LocalDate currentStartDate = couponDto.getStartDate();
        LocalDate currentEndDate = couponDto.getEndDate();
        int currentAmount = couponDto.getAmount();
        double currentPrice =couponDto.getPrice();
        String currentImage= couponDto.getImage();

        //Current Coupon details
        CouponDto couponFromDb =utils.convertToDto(couponRepository
                .getReferenceById(couponDto.getId()), CouponDto.class);

        String previousCompany = couponFromDb.getCompany().getEmail();
        String previousCategory = couponFromDb.getCategory().toString();
        String previousTitle = couponFromDb.getTitle();
        String previousDescription = couponFromDb.getDescription();
        LocalDate previousStartDate = couponFromDb.getStartDate();
        LocalDate previousEndDate = couponFromDb.getEndDate();
        int previousAmount = couponFromDb.getAmount();
        double previousPrice = couponFromDb.getPrice();
        String previousImage= couponFromDb.getImage();

       return currentCompany.equals(previousCompany) && currentCategory.equals(previousCategory)
                && currentTitle.equals(previousTitle) && currentDescription.equals(previousDescription)
                && currentStartDate.equals(previousStartDate) && currentEndDate.equals(previousEndDate)
                && currentAmount == previousAmount && currentPrice == previousPrice && currentImage.equals(previousImage);
    }
}
