import appConfig from "../Config/AppConfig";
import axiosInstance from "./Axios/AxiosInstance.ts";
import {Customer} from "../Models/Customer";

const URL = appConfig.apiAddress + "customer";
const axiosInterceptor = axiosInstance;

export async function addCustomerApi(customer: Customer) {
    return (await axiosInterceptor.post(`${URL}`, customer)).data as Customer;
}

export async function updateCustomerApi(customer: Customer) {
    return (await axiosInterceptor.put(`${URL}`, customer)).data as Customer;
}

export async function deleteCustomerApi(customerId: number) {
    await axiosInterceptor.delete(`${URL}/${customerId}`);
}

export async function getAllCustomersApi() {
    return (await axiosInterceptor.get(`${URL}/all`)).data as Customer[];
}

export async function getAllCustomersByCouponCategoryApi(categoryId: number) {
    return (await axiosInterceptor.get(`${URL}/getAllCustomersByCouponCategory/${categoryId}`)).data as Customer[];
}

export async function getOneCustomerApi(customerId: number) {
    return (await axiosInterceptor.get(`${URL}/getSingle/${customerId}`)).data as Customer;
}