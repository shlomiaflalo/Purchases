package com.johnbryce.coupon_system_final.db;

import com.johnbryce.coupon_system_final.dto.CategoryDto;
import com.johnbryce.coupon_system_final.dto.CompanyDto;
import com.johnbryce.coupon_system_final.dto.CouponDto;
import com.johnbryce.coupon_system_final.dto.CustomerDto;
import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.security.ClientType;
import com.johnbryce.coupon_system_final.security.TokenInformation;
import com.johnbryce.coupon_system_final.security.TokenManager;
import com.johnbryce.coupon_system_final.services.category.CategoryService;
import com.johnbryce.coupon_system_final.services.company.CompanyService;
import com.johnbryce.coupon_system_final.services.coupon.CouponService;
import com.johnbryce.coupon_system_final.services.customer.CustomerService;
import com.johnbryce.coupon_system_final.services.purchase.PurchaseService;
import com.johnbryce.coupon_system_final.utils.CompanyGenerate;
import com.johnbryce.coupon_system_final.utils.DataGenerator;
import com.johnbryce.coupon_system_final.utils.UserGenerate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Order(1)
@RequiredArgsConstructor
public class InsertingDataToDB implements CommandLineRunner {

    private final CompanyService companyService;
    private final CustomerService customerService;
    private final CouponService couponService;
    private final PurchaseService purchaseService;
    private final CategoryService categoryService;
    private final TokenManager tokenManager;
    private final AdminTokenInternalUse adminTokenInternalUse;

    private static final int MAX_PURCHASES = 5;

    private void generateDummyCompanies() throws Exception {
        int sizeOfRandomCompanies = ThreadLocalRandom.current().nextInt(15,
                20);

        List<CompanyGenerate> companies =
                DataGenerator.companyEmailNameListNoDuplicate(sizeOfRandomCompanies);

        for (int i = 0; i < sizeOfRandomCompanies; i++) {
            String name = companies.get(i).getName();
            String email = companies.get(i).getEmail();

            CompanyDto company = CompanyDto.builder()
                    .name(name).
                    email(email).
                    password(DataGenerator.randomPassword()).build();

            //Creating token for each created company
            TokenInformation tokenInformation = new TokenInformation();
            tokenInformation.setId(i+1);
            tokenInformation.setEmail(company.getEmail());
            tokenInformation.setName(company.getName());
            tokenInformation.setClientType(ClientType.COMPANY);
            tokenManager.addToken(tokenInformation);

            companyService.add(adminTokenInternalUse.getAdminToken(),company);
        }
    }

    private void generateDummyCoupons() throws Exception {
        int randomCouponsLength = ThreadLocalRandom.current().nextInt(10,
                15);
        List<CompanyDto> companies= companyService.getAll(adminTokenInternalUse.getAdminToken());

        for (int i = 0; i < randomCouponsLength; i++) {
            int couponsAmount = ThreadLocalRandom.current().nextInt(10, 500);
            double priceForCoupon = ThreadLocalRandom.current().nextInt(50, 700);
            int plusDaysRandom = ThreadLocalRandom.current().nextInt(5, 15);
            CompanyDto company = companies.get(i);
            CategoryDto categoryDto = categoryService.getRandomCategory();

            CouponDto couponDto = CouponDto.builder().
                    company(company).
                    category(categoryDto).
                    title(DataGenerator.titleForCoupon()).
                    description(DataGenerator.descriptionForCoupon()).
                    startDate(LocalDate.now()).
                    endDate(LocalDate.now().plusDays(plusDaysRandom)).
                    amount(couponsAmount).
                    price(priceForCoupon).
                    image("Image " + i).build();

            couponService.add(tokenManager.tokenFromEmail(company.getEmail()),couponDto);
        }
    }

    private void generateDummyCustomers() throws Exception {
        int randomCustomersLength = ThreadLocalRandom.current().nextInt(10,
                15);

        List<UserGenerate> userGenerates =
                DataGenerator.emailFirstLastNameListNoDuplicate(randomCustomersLength);

        for (int i = 0; i < randomCustomersLength; i++) {

            String firstName = userGenerates.get(i).getFirstName();
            String lastName = userGenerates.get(i).getLastName();
            String email = userGenerates.get(i).getEmail();

            CustomerDto customerDto = CustomerDto.builder().
                    firstName(firstName).
                    lastName(lastName).
                    email(email).
                    password(DataGenerator.randomPassword()).build();

            //Creating token for each created customer
            TokenInformation tokenInformation = new TokenInformation();
            tokenInformation.setId(i+1);
            tokenInformation.setEmail(customerDto.getEmail());
            tokenInformation.setName(customerDto.getFirstName());
            tokenInformation.setClientType(ClientType.CUSTOMER);
            tokenManager.addToken(tokenInformation);

            customerService.add(adminTokenInternalUse.getAdminToken(),customerDto);
        }
    }


    public void initializationCategories() throws Exception {
        for (String category : DataGenerator.getCategoriesForCategoriesEntity()) {
            CategoryDto categoryDto = CategoryDto.builder().
                    name(category.toLowerCase()).build();
            categoryService.add(adminTokenInternalUse.getAdminToken(),categoryDto);
        }
    }

    private void generateDummyPurchases() throws CouponSystemException {
        List<CustomerDto> customers= customerService.getAll(adminTokenInternalUse.getAdminToken());

        for (int i = 1, j = 1; i <= MAX_PURCHASES && j <= MAX_PURCHASES; i++, j++) {
            //i + j is for the customerId & couponId
            //in the purchaseCoupon method. i-> customerId j-> couponId

            CustomerDto customerDto = customers.get(i);

            try {
                purchaseService.purchaseCoupon(tokenManager.tokenFromEmail(customerDto.getEmail()), j);
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }

        }
    }


    public void initializeAllData() throws Exception {
        initializationCategories();
        generateDummyCompanies();
        generateDummyCoupons();
        generateDummyCustomers();
        generateDummyPurchases();
    }

    @Override
    //Will run first by order(1)
    public void run(String... args) throws Exception {
        initializeAllData();
    }


}
