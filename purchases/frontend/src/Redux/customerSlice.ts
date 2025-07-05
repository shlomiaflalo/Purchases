import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {Coupon} from "../Models/Coupon.ts";
import {Customer} from "../Models/Customer.ts";
import {Category} from "../Models/Category.ts";
import notificationService from "../Components/NotificationService/NotificationService.tsx";
import {Company} from "../Models/Company.ts";

const loadCustomer = (() => {
    try {
        const customer = localStorage.getItem('customer');

        if (customer) {
            return JSON.parse(customer);
        }
    } catch {
        notificationService.infoPlainText('Error loading customer');
    }
    return {
        id: 0,
        firstName: "",
        lastName: "",
        email: "",
        password: "",
    }
})();

interface CustomerState {
    purchases: Coupon[];
    categories: Category[];
    companies: Company[],
    allCoupons: Coupon[],
    customer: Customer;
}

const initialState: CustomerState = {
    purchases: [],
    categories: [],
    companies: [],
    allCoupons: [],
    customer: loadCustomer,
};

const customerSlice = createSlice({
    name: 'customer',
    initialState,
    reducers: {
        addPurchase: (state, action: PayloadAction<Coupon>) => {
            state.purchases.push(action.payload);
        },
        setAllCoupons: (state, action: PayloadAction<Coupon[]>) => {
            state.allCoupons = action.payload;
        },
        updateCouponPurchase: (state, action: PayloadAction<Coupon>) => {
            const index = state.allCoupons.findIndex(c => c.id === action.payload.id);
            if (index >= 0) state.allCoupons[index] = action.payload;
        },
        setAllPurchases: (state, action: PayloadAction<Coupon[]>) => {
            state.purchases = action.payload;
        },
        addCategoriesCustomer(state, action: PayloadAction<Category[]>) {
            state.categories = action.payload;
        },
        addCompaniesCustomer(state, action: PayloadAction<Company[]>) {
            state.companies = action.payload;
        },
        updateCustomerDetails: (state, action: PayloadAction<Customer>) => {
            state.customer = action.payload;
            localStorage.setItem('customer', JSON.stringify(action.payload));

        },
        addCustomerDetails: (state, action: PayloadAction<Customer>) => {
            state.customer = action.payload;
            localStorage.setItem('customer', JSON.stringify(action.payload));
        },
        clearCustomerData: (state) => {
            state.purchases = [];
            state.categories = [];
            state.companies = [];
            state.allCoupons = [];
            state.customer = {
                id: 0,
                firstName: "",
                lastName: "",
                email: "",
                password: "",
            };
        }
    },
});

export const {
    addPurchase,
    setAllCoupons,
    clearCustomerData,
    updateCustomerDetails,
    addCustomerDetails,
    setAllPurchases,
    addCategoriesCustomer,
    updateCouponPurchase,
    addCompaniesCustomer
} = customerSlice.actions;

export default customerSlice.reducer;


