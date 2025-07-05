import appConfig from "../Config/AppConfig";
import axiosInstance from "./Axios/AxiosInstance.ts";
import {Company} from "../Models/Company";

const URL = appConfig.apiAddress + "company";
const axiosInterceptor = axiosInstance;

export async function addCompanyApi(company: Company) {
    return (await axiosInterceptor.post(`${URL}`, company)).data as Company;
}

export async function updateCompanyApi(company: Company) {
    return (await axiosInterceptor.put(`${URL}`, company)).data as Company;
}

export async function deleteCompanyApi(companyId: number) {
    await axiosInterceptor.delete(`${URL}/${companyId}`);
}

export async function getAllCompaniesApi() {
    return (await axiosInterceptor.get(`${URL}/all`)).data as Company[];
}

export async function getOneCompanyApi(companyId: number) {
    return (await axiosInterceptor.get(`${URL}/${companyId}`)).data as Company;
}

export async function getCompanyByEmailApi(email: string) {
    return (await axiosInterceptor.get(`${URL}/getSingleByEmail?email=${email}`)).data as Company;
}