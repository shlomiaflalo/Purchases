import appConfig from "../Config/AppConfig";
import axiosInstance from "./Axios/AxiosInstance.ts";
import {Purchase} from "../Models/Purchase";

const URL = appConfig.apiAddress + "purchase";
const axiosInterceptor = axiosInstance;

export async function purchaseCouponApi(couponId: number) {
    await axiosInterceptor.post(`${URL}/${couponId}`);
}

export async function deleteCouponPurchaseApi(customerId: number, couponId: number) {
    await axiosInterceptor.delete(`${URL}/deleteCouponPurchase/${customerId}/${couponId}`);
}

export async function deleteCouponPurchasesByCouponIdApi(couponId: number) {
    await axiosInterceptor.delete(`${URL}/deleteCouponPurchasesByCouponId/${couponId}`);
}

export async function deleteCouponPurchaseByCompanyIdApi() {
    await axiosInterceptor.delete(`${URL}/deleteCouponPurchaseByCompanyId`);
}

export async function deleteCouponPurchaseByCustomerIdApi(customerId: number) {
    await axiosInterceptor.delete(`${URL}/deleteCouponPurchaseByCustomerId/${customerId}`);
}

export async function updatePurchaseApi(purchase: Purchase) {
    return (await axiosInterceptor.put(`${URL}`, purchase)).data as Purchase;
}

export async function getAllPurchasesApi() {
    return (await axiosInterceptor.get(`${URL}/all`)).data as Purchase[];
}

export async function getOnePurchaseApi(purchaseId: number) {
    return (await axiosInterceptor.get(`${URL}/${purchaseId}`)).data as Purchase;
}