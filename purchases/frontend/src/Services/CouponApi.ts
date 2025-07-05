import {Coupon} from "../Models/Coupon";

async function getAxiosAndUrl() {
    //Lazy loading to break any import loop
    const [{default: appConfig}, {default: axiosInstance}] = await Promise.all([
        import("../Config/AppConfig"),
        import("./Axios/AxiosInstance")
    ]);

    const URL = appConfig.apiAddress + "coupon";
    return {axiosInterceptor: axiosInstance, URL};
}

export async function getCompanyCouponsByCompanyApi() {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCompanyCouponsByCompany`)).data as Coupon[];
}

export async function getCompanyCouponsByCustomerApi(companyId: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCompanyCouponsByCustomer/${companyId}`)).data as Coupon[];
}

export async function getCompanyCouponsByCompanyMaxPriceApi(maxPrice: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCompanyCouponsByMaxPriceByCompany?maxPrice=${maxPrice}`)).data as Coupon[];
}

export async function getCompanyCouponsByCustomerMaxPriceApi(companyId: number, maxPrice: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCompanyCouponsByMaxPriceByCustomer/${companyId}?maxPrice=${maxPrice}`)).data as Coupon[];
}

export async function getCustomerCouponsByMaxPriceApi(maxPrice: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCouponsByMaxPriceAndCustomerId?maxPrice=${maxPrice}`)).data as Coupon[];
}

export async function getOneCouponApi(couponId: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/${couponId}`)).data as Coupon;
}

export async function getAllCouponsApi() {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/all`)).data as Coupon[];
}

export async function getCustomerCouponsByCategoryIdApi(categoryId: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCustomerCouponsByCategoryId/${categoryId}`)).data as Coupon[];
}

export async function getCustomerCouponsApi() {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCustomerCoupons`)).data as Coupon[];
}

export async function addCouponByCompanyApi(coupon: Coupon) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.post(`${URL}/addCouponByCompany`, coupon)).data as Coupon;
}

export async function updateCouponByCompanyApi(coupon: Coupon) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.put(`${URL}/updateCouponByCompany`, coupon)).data as Coupon;
}

export async function deleteCouponByCompanyApi(couponId: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    await axiosInterceptor.delete(`${URL}/deleteCouponByCompany/${couponId}`);
}

export async function getCompanyCouponsByCategoryIdAndByCompanyApi(categoryId: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCompanyCouponsByCategoryIdAndByCompany/${categoryId}`)).data as Coupon[];
}

export async function getCompanyCouponsByCategoryIdAndByCustomerApi(categoryId: number, companyId: number) {
    const {axiosInterceptor, URL} = await getAxiosAndUrl();
    return (await axiosInterceptor.get(`${URL}/getCompanyCouponsByCategoryIdAndByCustomer/${companyId}/${categoryId}`)).data as Coupon[];
}