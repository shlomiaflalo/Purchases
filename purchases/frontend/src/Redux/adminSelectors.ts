import {RootState} from "./Store.ts";

export const selectAllCompanies = (state: RootState) => state.admin.companies;
export const selectAllCustomers = (state: RootState) => state.admin.customers;

export const selectCompanyByEmail = (email: string) => (state: RootState) =>
    state.admin.companies.find(c => c.email === email);

export const selectCustomerByEmail = (email: string) => (state: RootState) =>
    state.admin.customers.find(c => c.email === email);

export const selectCompanyById = (id: number) => (state: RootState) =>
    state.admin.companies.find(c => c.id === id);

export const selectCustomerById = (id: number) => (state: RootState) =>
    state.admin.customers.find(c => c.id === id);