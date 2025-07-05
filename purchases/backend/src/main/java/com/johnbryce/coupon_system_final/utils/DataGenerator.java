package com.johnbryce.coupon_system_final.utils;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class DataGenerator {

    private static final Faker faker = new Faker();

    public static List<String> getCategoriesForCategoriesEntity() {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            String category = getRandomCategory();
            while (categories.contains(category)) {
                category = getRandomCategory();
            }
            categories.add(category);
        }
        return categories;
    }

    public static String getRandomCategory() {
        return faker.commerce().department().split("[ ,]", 2)[0];
    }

    public static String randomPassword() {
        return faker.internet().password();
    }

    public static List<UserGenerate> emailFirstLastNameListNoDuplicate(int numberOfUsers) {
        List<UserGenerate> fullNameEmailList = new ArrayList<>();

        for (int i = 0; i < numberOfUsers; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            for (i = 0; i < fullNameEmailList.size(); i++) {
                while (fullNameEmailList.get(i).getFirstName().equals(firstName)) {
                    firstName = faker.name().firstName();
                }
                while (fullNameEmailList.get(i).getLastName().equals(lastName)) {
                    lastName = faker.name().lastName();
                }
            }

            String email = faker.internet().emailAddress(firstName + "." + lastName);

            UserGenerate userGenerate = UserGenerate.builder()
                    .firstName(firstName).
                    lastName(lastName).email(email).build();

            fullNameEmailList.add(userGenerate);
        }
        return fullNameEmailList;
    }

    public static List<CompanyGenerate> companyEmailNameListNoDuplicate(int numberOfCompanies) {
        List<CompanyGenerate> companies = new ArrayList<>();

        for (int i = 0; i < numberOfCompanies; i++) {

            String companyName = faker.company().name();
            String email = faker.internet().emailAddress(companyName);

            email = email.replaceAll(" ", "").
                    replaceAll(",", "").replaceAll("-", "");

            for (i = 0; i < companies.size(); i++) {
                while (companies.get(i).getEmail().equals(email)) {
                    companyName = faker.company().name();
                    email = faker.internet().emailAddress(companyName);
                    email = email.replaceAll(" ", "").
                            replaceAll(",", "").replaceAll("-", "");
                }
            }

            CompanyGenerate companyGenerate = CompanyGenerate.builder()
                    .name(companyName).
                    email(email).build();

            companies.add(companyGenerate);
        }
        return companies;
    }

    public static String titleForCoupon() {
        int amount = faker.number().numberBetween(20, 100);
        String product = faker.commerce().productName();
        return String.format("%s on orders over $%d", product, amount);
    }

    public static String descriptionForCoupon() {
        String[] description = new String[]{
                faker.rickAndMorty().character() + " selling it ",
                faker.dog().name() + " from " + faker.country().name() + " selling it ",
                faker.animal().name() + " from the book " + faker.book().title() + " is selling it "
        };
        return description[faker.number().numberBetween(0, description.length - 1)];
    }

    public static String belongTo() {
        return faker.harryPotter().character();
    }

    public static int yearGenerator() {
        int randomYear = faker.number().numberBetween(10, 50);
        LocalDateTime localDateTime = LocalDateTime.now().minusYears(randomYear);
        return localDateTime.getYear();
    }


}
