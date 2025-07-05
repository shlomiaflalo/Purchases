import "./MenuButtonsForCompany.css"
import {useNavigate} from "react-router-dom";

function MenuButtonsForCompany() {

    const navigate = useNavigate();

    const navigateHome = () => {
        navigate("/company");
    };

    const navigateToAddCoupon = () => {
        navigate("/company/add-coupon");
    };
    const navigateToMyCoupons = () => {
        navigate("/company/my-coupons");
    };
    const navigateToMyProfile = () => {
        navigate("/company/my-profile");
    };

    return (
        <div className="company_side_bar_buttons">
            <button onClick={navigateHome}>Home</button>
            <button onClick={navigateToAddCoupon}>Add coupon</button>
            <button onClick={navigateToMyCoupons}>My coupons</button>
            <button onClick={navigateToMyProfile}>My profile</button>
        </div>
    )
}

export default MenuButtonsForCompany;