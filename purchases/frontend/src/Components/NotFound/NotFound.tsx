import "./NotFound.css"
import errorImg from "../../assets/CouponMood3.png";
import {useNavigate} from "react-router-dom";

const NotFound = () => {
    const navigate = useNavigate();

    const handleGoBack = () => {
        navigate("/");
    };

    return (
        <div className="not-found">
            <img
                src={errorImg}
                alt="404"
            />
            <h1>404 - Page Not Found</h1>
            <p>The page you're looking for doesn't exist</p>
            <button onClick={handleGoBack} className={"button-go-back-auth"}>Go back</button>
        </div>
    );
};

export default NotFound;
