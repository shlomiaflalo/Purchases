import "./MenuButtonsForCustomer.css"
import {useNavigate} from "react-router-dom";

function MenuButtonsForCustomer() {

    const navigate = useNavigate();

    const navigateHome = () => {
        navigate("/customer");
    };
    const navigateToMyCoupons = () => {
        navigate("/customer/my-coupons");
    };
    const navigateToAllCoupons = () => {
        navigate("/customer/coupons");
    };
    const navigateToMyProfile = () => {
        navigate("/customer/my-profile");
    };
    return (
        <div className="customer_side_bar_buttons">
            <button onClick={navigateHome}>Home</button>
            <button onClick={navigateToAllCoupons}>Show coupons</button>
            <button onClick={navigateToMyCoupons}>My coupons</button>
            <button onClick={navigateToMyProfile}>My profile</button>
        </div>
    )
}

export default MenuButtonsForCustomer;