import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {Coupon} from "../Models/Coupon.ts";
import {Company} from "../Models/Company.ts";
import {Category} from "../Models/Category.ts";
import notificationService from "../Components/NotificationService/NotificationService.tsx";

const loadCompany = (() => {
    try {
        const company = localStorage.getItem('company');

        if (company) {
            return JSON.parse(company);
        }
    } catch {
        notificationService.infoPlainText('Error loading company');
    }
    return {
        id: 0,
        name: "",
        email: "",
        password: "",
        couponCount: 0,
    }
})();

interface CompanyState {
    coupons: Coupon[];
    categories: Category[];
    company: Company;
}

const initialState: CompanyState = {
    coupons: [],
    categories: [],
    company: loadCompany,
};

const companySlice = createSlice({
    name: 'company',
    initialState,
    reducers: {
        addCoupons(state, action: PayloadAction<Coupon[]>) {
            state.company.couponCount = action.payload.length;
            state.coupons = action.payload;
        },
        addCategoriesCompany(state, action: PayloadAction<Category[]>) {
            state.categories = action.payload;
        },
        addCoupon: (state, action: PayloadAction<Coupon>) => {
            state.company.couponCount = (state.company.couponCount!) + 1;
            state.coupons.push(action.payload);
        },
        updateCoupon: (state, action: PayloadAction<Coupon>) => {
            const index = state.coupons.findIndex(c => c.id === action.payload.id);
            if (index >= 0) state.coupons[index] = action.payload;
        },
        deleteCoupon: (state, action: PayloadAction<number>) => {
            state.company.couponCount = (state.company.couponCount!) - 1;
            state.coupons = state.coupons.filter(c => c.id !== action.payload);
        },
        updateCompanyDetails: (state, action: PayloadAction<Company>) => {
            state.company = action.payload;
            localStorage.setItem('company', JSON.stringify(action.payload));
        },
        addCompanyDetails: (state, action: PayloadAction<Company>) => {
            state.company = action.payload;
            localStorage.setItem('company', JSON.stringify(action.payload));
        },

        clearCompanyData: (state) => {
            state.coupons = [];
            state.categories = [];
            state.company = {
                id: 0,
                name: "",
                email: "",
                password: "",
                couponCount: 0,
            };
        },
    },
});


export const {
    addCoupon,
    deleteCoupon,
    updateCoupon,
    clearCompanyData,
    addCoupons,
    updateCompanyDetails,
    addCompanyDetails,
    addCategoriesCompany
} = companySlice.actions;

export default companySlice.reducer;
