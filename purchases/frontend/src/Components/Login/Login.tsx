import "./Login.css"
import {useForm} from "react-hook-form";
import {LoginRequest} from "../../Models/LoginRequest.ts";
import {useNavigate} from "react-router-dom";
import {loginApi} from "../../Services/LoginApi.tsx";
import {useDispatch} from "react-redux";
import {loginAuth} from "../../Redux/authSlice.ts";
import notificationService from "../NotificationService/NotificationService.tsx";

function Login() {

    const {register,handleSubmit,reset,watch} = useForm<LoginRequest>();
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const email = watch("email");
    const password = watch("password");
    const isFormValid = email?.trim() && password?.trim();


    const onSubmit =  (loginRequest: LoginRequest) => {
        loginApi(loginRequest).then(
            (result) => {

                dispatch(loginAuth(result));
                const userType = result.clientType;

                switch (userType) {
                    case "ADMINISTRATOR":
                        navigate("/administrator");
                        break;
                    case "COMPANY":
                        navigate("/company");
                        break;
                    case "CUSTOMER":
                        navigate("/customer");
                        break;
                }

                const userTypeForMsg = userType.toLowerCase();
                notificationService.successfulLogin(`Logged in as ${userTypeForMsg}`,userTypeForMsg);
            }
        ).catch((error) => {
            notificationService.errorAxiosApiCall(error);
            reset();
        });
    }

    return (
            <div className="full-background">
                <div className="login-page">
                    <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
                        <input type="email" placeholder="Email" className="input-field" required {
                            ...register("email")}/>

                        <input type="password" placeholder="Password" className="input-field" required {
                            ...register("password")}/>

                        <button type="submit" disabled={!isFormValid}>Login</button>
                    </form>
                </div>
            </div>
    );
}

export default Login;
