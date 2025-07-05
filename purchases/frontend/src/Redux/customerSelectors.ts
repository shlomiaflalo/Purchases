import {RootState} from "./Store.ts";

export const selectCompanyCoupons = (companyName: string) => (state: RootState) =>
    state.company.coupons.filter(coupon => coupon.company?.name == companyName);

export const selectCustomerPurchasesByCategoryName = (categoryName: string) => (state: RootState) =>
    state.customer.purchases.filter(coupon => coupon.category.name == categoryName);

export const selectCustomerCouponsByMaxPrice = (maxPrice: number) => (state: RootState) =>
    state.customer.purchases.filter(coupon => coupon.price <= maxPrice);

export const selectCustomerCoupons = (state: RootState) =>
    state.customer.purchases;

export const selectAllCoupons = (state: RootState) => {
    return state.customer.allCoupons;
}

export const selectOneCouponById = (couponId: number) => (state: RootState) =>
    state.customer.allCoupons.filter(coupon => coupon.id === couponId);

export const selectOneCouponByTitle = (couponTitle: string) => (state: RootState) =>
    state.customer.allCoupons.filter(coupon => coupon.title === couponTitle);

export const selectCompanyCouponsByCustomerFilteredByMaxPrice = (maxPrice: number, companyName: string) => (state: RootState) =>
    state.customer.purchases.filter(coupon => coupon.company?.name === companyName && coupon.price <= maxPrice);

export const selectCompanyCouponsByCategoryIdAndByCustomer = (categoryName: string, companyName: string) => (state: RootState) =>
    state.customer.purchases.filter(coupon => coupon.company?.name === companyName && coupon.category.name === categoryName);