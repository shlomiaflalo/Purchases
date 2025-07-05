import axios from 'axios';
import {store} from "../../Redux/Store";
import notificationService from "../../Components/NotificationService/NotificationService.tsx";
import {navigateTo} from "../../Utils/navigationService.ts";
import {clearReduxAndAuthFactory} from "../../Utils/ClearReduxAndAuth.ts";

const axiosInstance = axios.create({
    baseURL: "http://localhost:4040/api/",
});

axiosInstance.interceptors.request.use(
    (config) => {

        const user = store.getState().auth.user;
        const state = store.getState();
        const clientType = state.auth.user?.clientType;
        const dispatch = store.dispatch;

        const userFromLocalStorage = localStorage.getItem('user');

        const now = new Date();
        const expire = new Date(user.expireTime);

        const nowTotalMinutes = Math.floor(now.getTime() / 60000);
        const expireTotalMinutes = Math.floor(expire.getTime() / 60000);

        const tokenExpired = nowTotalMinutes >= expireTotalMinutes;

        if (config.url?.includes("/client/login")) {
            return config;
        }

        if (tokenExpired) {
            notificationService.warningPlainText("Token expired");
            setTimeout(() => {
                delete config.headers.Authorization;
                clearReduxAndAuthFactory(clientType, dispatch);
                navigateTo("/home");
            }, 5000);
            return Promise.reject(new axios.Cancel("Request cancelled due to token expiration time"));
        }

        if (user.token && userFromLocalStorage) {
            config.headers = config.headers || {};
            config.headers.Authorization = `${user.token}`;
            return config;
        } else {
            //If removed manually, the token to check on local storage
            notificationService.warningPlainText("Token removed manually, redirecting home");
            setTimeout(() => {
                delete config.headers.Authorization;
                clearReduxAndAuthFactory(clientType, dispatch);
                navigateTo("/home");
            }, 5000);
            return Promise.reject(new axios.Cancel("Request cancelled due to missing token"));
        }
    },
    (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        const state = store.getState();
        const clientType = state.auth.user?.clientType;
        const dispatch = store.dispatch;

        if (axios.isCancel(error)) {
            // 🔕 Silently ignore canceled requests
            return new Promise(() => {
            });
        }

        if (error.response?.data?.code === 1032) {
            notificationService.errorAxiosApiCall(error.response.data.message);
            clearReduxAndAuthFactory(clientType, dispatch);
            setTimeout(() => {
                navigateTo("/home");
            }, 5000);
            return;
        }
        if (axios.isCancel(error)) {
            return new Promise(() => {});  // Ignore canceled requests
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;


