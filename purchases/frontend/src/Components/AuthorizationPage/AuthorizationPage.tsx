import "./AuthorizationPage.css";
import errorImg from "../../assets/CouponMood2.png";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";

const AuthorizationPage = () => {
    const navigate = useNavigate();
    const fromApp = sessionStorage.getItem("fromApp") === "true";

    const handleGoBack = () => {
        navigate("/");
    };

    useEffect(() => {
        if (!fromApp) {
            navigate("/", {replace: true});
        } else {
            sessionStorage.removeItem("fromApp");
        }
    }, [fromApp, navigate]);

    if (!fromApp) return null;

    return (
        <div className="authorization-failed">
            <img src={errorImg} alt="403"/>
            <h1>403 - Authorization failed</h1>
            <p>You're not allowed to access this page</p>
            <button onClick={handleGoBack} className={"button-go-back-auth"}>Go back</button>
        </div>
    );
};

export default AuthorizationPage;
