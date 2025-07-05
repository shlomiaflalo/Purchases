import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {Customer} from "../Models/Customer.ts";
import {Company} from "../Models/Company.ts";

interface AdminState {
    customers: Customer[];
    companies: Company[];
}

const initialState: AdminState = {
    customers: [],
    companies: [],
};

const adminSlice = createSlice({
    name: 'admin',
    initialState,
    reducers: {
        // Customers
        setCustomers: (state, action: PayloadAction<Customer[]>) => {
            state.customers = action.payload;
        },
        addCustomer: (state, action: PayloadAction<Customer>) => {
            state.customers.push(action.payload);
        },
        updateCustomer: (state, action: PayloadAction<Customer>) => {
            const index = state.customers.findIndex(c => c.id === action.payload.id);
            if (index >= 0) state.customers[index] = action.payload;
        },
        deleteCustomer: (state, action: PayloadAction<number>) => {
            state.customers = state.customers.filter(c => c.id !== action.payload);
        },

        // Companies
        setCompanies: (state, action: PayloadAction<Company[]>) => {
            state.companies = action.payload;
        },
        addCompany: (state, action: PayloadAction<Company>) => {
            state.companies.push(action.payload);
        },
        updateCompany: (state, action: PayloadAction<Company>) => {
            const index = state.companies.findIndex(c => c.id === action.payload.id);
            if (index >= 0) state.companies[index] = action.payload;
        },
        deleteCompany: (state, action: PayloadAction<number>) => {
            state.companies = state.companies.filter(c => c.id !== action.payload);
        },

        clearAdminData: (state) => {
            state.customers = [];
            state.companies = [];
        },
    },
});

export const {
    setCustomers,
    addCustomer,
    updateCustomer,
    deleteCustomer,
    setCompanies,
    addCompany,
    updateCompany,
    deleteCompany,
    clearAdminData,
} = adminSlice.actions;

export default adminSlice.reducer;