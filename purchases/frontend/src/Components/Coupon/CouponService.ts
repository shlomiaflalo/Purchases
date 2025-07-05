import {Coupon} from "../../Models/Coupon.ts";
import React from 'react';

class CouponService {

    public validLogicValueNumber(e: React.KeyboardEvent<HTMLInputElement>) {
        const isFirstChar = e.currentTarget.selectionStart === 0;

        // Block '0' as first character when field is empty or cursor is at start
        if (e.key === "0" && isFirstChar && !e.currentTarget.value) {
            e.preventDefault();
            return;
        }

        // Block other invalid characters
        const invalidChars = ["-", "+", "e", "E", ".", ","];
        if (invalidChars.includes(e.key)) {
            e.preventDefault();
        }
    }

    public validNumberInput(payload: string): boolean {
        const str = payload.trim();

        // Allow empty string so input can be cleared
        if (str === "") return true;

        const isAllDigits = /^\d+$/.test(str);
        const startsWithZero = /^0/.test(str);

        return isAllDigits && !startsWithZero;
    }

    public isFormValid(coupon: Partial<Coupon>): boolean {
        return !!(
            coupon.category?.name?.trim().length &&
            coupon.title?.trim().length &&
            coupon.description?.trim().length &&
            coupon.price &&
            coupon.amount &&
            coupon.startDate &&
            coupon.endDate
        );
    }

    public hasChanges = (couponA: Coupon, couponB: Coupon) => {
        if (!couponA || !couponB) return false;

        return (
            couponA.category.name.trim() !== couponB.category.name.trim() ||
            couponA.title.trim() !== couponB.title.trim() ||
            couponA.description.trim() !== couponB.description.trim() ||
            couponA.startDate !== couponB.startDate ||
            couponA.endDate !== couponB.endDate ||
            couponA.amount !== couponB.amount ||
            couponA.price !== couponB.price);
    };
}

const couponService = new CouponService();
export default couponService;
