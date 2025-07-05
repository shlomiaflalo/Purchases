import {RootState} from "./Store.ts";

export const selectCouponsByMaxPrice = (maxPrice: number) => (state: RootState) =>
    state.company.coupons.filter(coupon => coupon.price <= maxPrice);

export const selectCompanyCoupons = (state: RootState) => state.company.coupons;

export const selectCouponsByCategoryName = (categoryName: string) => (state: RootState) =>
    state.company.coupons.filter(coupon => coupon.category.name == categoryName);
