import appConfig from "../Config/AppConfig";
import axiosInstance from "./Axios/AxiosInstance.ts";
import {LoginRequest} from "../Models/LoginRequest";
import {LoginResponse} from "../Models/LoginResponse";


const URL = appConfig.apiAddress + "client";
const axiosInterceptor = axiosInstance;

export async function loginApi(loginRequest: LoginRequest): Promise<LoginResponse> {
    return (await axiosInterceptor.post(`${URL}/login`, loginRequest)).data;
}

