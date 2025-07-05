import appConfig from "../Config/AppConfig";
import axiosInstance from "./Axios/AxiosInstance.ts";
import {Category} from "../Models/Category";

const URL = appConfig.apiAddress + "category";
const axiosInterceptor = axiosInstance;

export async function addCategoryApi(category: Category) {
    return (await axiosInterceptor.post(`${URL}`, category)).data as Category;
}

export async function updateCategoryApi(category: Category) {
    return (await axiosInterceptor.put(`${URL}`, category)).data as Category;
}

export async function deleteCategoryApi(categoryId: number) {
    await axiosInterceptor.delete(`${URL}/${categoryId}`);
}

export async function getAllCategoriesApi() {
    return (await axiosInterceptor.get(`${URL}/all`)).data as Category[];
}

export async function getOneCategoryApi(categoryId: number) {
    return (await axiosInterceptor.get(`${URL}/${categoryId}`)).data as Category;
}