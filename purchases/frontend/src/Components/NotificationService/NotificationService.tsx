import {toast} from "react-toastify";
import success from "../../assets/Coupon.png";
import info from "../../assets/CouponMood1.png";
import warning from "../../assets/CouponMood2.png";
import error from "../../assets/CouponMood3.png";
import adminLogin from "../../assets/CouponMood4.png";
import customerLogin from "../../assets/Customer.png";
import companyLogin from "../../assets/Company.png";
import "./NotificationService.css"
import axios, {AxiosError} from "axios";
import {navigateTo} from "../../Utils/navigationService.ts";
import {store} from "../../Redux/Store.ts";
import {clearReduxAndAuthFactory} from "../../Utils/ClearReduxAndAuth.ts";

class NotificationService {

    public successfulLogin(message: string, clientType: string): void {
        let alt;
        let img;
        switch (clientType) {
            case "administrator":
                alt = "adminLogin";
                img = adminLogin;
                break;
            case "company":
                alt = "companyLogin".toLowerCase();
                img = companyLogin;
                break;
            case "customer":
                alt = "customerLogin".toLowerCase();
                img = customerLogin;
                break;
        }

        toast.success(
            <div className="toast-inner">
                <img src={img} alt={alt} className="custom-icon"/>
                <p>{message}</p>
            </div>
        );
    }

    public successPlainText(message: string): void {
        toast.success(
            <div className="toast-inner">
                <img src={success} alt="success" className="custom-icon"/>
                <p>{message}</p>
            </div>
        );
    }

    public warningPlainText(message: string): void {
        toast.warning(
            <div className={"toast-inner"}>
                <img src={warning} alt="warning" className="custom-icon"/>
                <p>{message}</p>
            </div>
        );
    }

    public infoPlainText(message: string): void {
        toast.info(
            <div className={"toast-inner"}>
                <img src={info} alt="info" className="custom-icon"/>
                <p>{message}</p>
            </div>
        );
    }

    public errorPlainText(message: string): void {
        toast.error(
            <div className={"toast-inner"}>
                <img src={error} alt="error" className="custom-icon"/>
                <p>{message}</p>
            </div>
        );
    }

    public errorAxiosApiCall(error: any): void {

        if (error instanceof AxiosError) {
            if (error.response) {
                const message = error.response.data?.message;
                this.errorPlainText(message);
            } else if (error.request) {
                const isNetworkError = error.code === "ECONNABORTED" || error.message === "Network Error";

                if (isNetworkError) {
                    this.errorPlainText("Server is not reachable, Please connect again later.");
                    const state = store.getState();
                    const clientType = state.auth.user?.clientType;
                    const dispatch = store.dispatch;
                    clearReduxAndAuthFactory(clientType, dispatch);
                    navigateTo("/home");
                    throw Promise.reject(new axios.Cancel("Request cancelled due to server connectivity"));
                }
            }

        }
    }
}

const notificationService = new NotificationService();
export default notificationService;
