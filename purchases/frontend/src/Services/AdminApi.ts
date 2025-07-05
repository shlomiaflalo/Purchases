import appConfig from "../Config/AppConfig";
import axiosInstance from "./Axios/AxiosInstance.ts";
import {Company} from "../Models/Company";
import {Customer} from "../Models/Customer";


const URL = appConfig.apiAddress + "admin";
const axiosInterceptor = axiosInstance;

export async function addCompanyApi(company: Company) {
    return (await axiosInterceptor.post(`${URL}/company`, company)).data as Company;
}

export async function addCustomerApi(customer: Customer) {
    return (await axiosInterceptor.post(`${URL}/customer`, customer)).data as Customer;
}

export async function updateCompanyApi(company: Company) {
    return (await axiosInterceptor.put(`${URL}/company`, company)).data as Company;
}

export async function updateCustomerApi(customer: Customer) {
    return (await axiosInterceptor.put(`${URL}/customer`, customer)).data as Customer;
}

export async function deleteCompanyApi(companyId: number) {
    await axiosInterceptor.delete(`${URL}/company/${companyId}`);
}

export async function deleteCustomerApi(customerId: number) {
    await axiosInterceptor.delete(`${URL}/customer/${customerId}`);
}

export async function getAllCompaniesApi() {
    return (await axiosInterceptor.get(`${URL}/getAllCompanies`)).data as Company[];
}

export async function getAllCustomersApi() {
    return (await axiosInterceptor.get(`${URL}/getAllCustomers`)).data as Customer[];
}

export async function getOneCompanyApi(companyId: number) {
    return (await axiosInterceptor.get(`${URL}/getOneCompany/${companyId}`)).data as Company;
}

export async function getOneCustomerApi(customerId: number) {
    return (await axiosInterceptor.get(`${URL}/getOneCustomer/${customerId}`)).data as Customer;
}

export async function getCompanyByEmailApi(email: string) {
    return (await axiosInterceptor.get(`${URL}/getSingleCompanyByEmail?companyEmail=${email}`)).data as Company;
}

export async function getCustomerByEmailApi(email: string) {
    return (await axiosInterceptor.get(`${URL}/getSingleCustomerByEmail?customerEmail=${email}`)).data as Customer;
}