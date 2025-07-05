import coupon from "../../assets/Coupon.png";
import "./CouponCard.css";
import "../../Utils/UserCard.css"
import {useNavigate} from "react-router-dom";
import {Category} from "../../Models/Category.ts";
import {formatDateForUser} from "../../MyDate/MyDateService.ts";
import {Company} from "../../Models/Company.ts";
import {useSelector} from "react-redux";
import {RootState} from "../../Redux/Store.ts";

interface couponProps {
    id?: number;
    company?: Company;
    category: Category;
    title: string;
    description: string;
    startDate: Date;
    endDate: Date;
    amount: number;
    price: number;
    clickable: boolean;
    disabled?: boolean;
}

function CouponCard(couponDetails: couponProps) {

    const navigate = useNavigate();
    const user = useSelector((state: RootState) => state.auth.user);

    const disable = couponDetails.disabled;
    const clickable = couponDetails.clickable;

    const navigateToCouponPage = () => {
        if (!couponDetails.disabled) {
            if (couponDetails.clickable) {
                if (user.clientType === "COMPANY") {
                    navigate(`/company/my-coupons/${couponDetails.id}`);
                } else if (user.clientType === "CUSTOMER") {
                    navigate(`/customer/coupons/${couponDetails.id}`);
                }
            }
        }
    };

    return (
        <div className={disable ? "card-disabled" : clickable ? "user-card-cursor" : ""}
             onClick={navigateToCouponPage}>
            <div className="user-card coupon-card">
                <img src={coupon} alt="coupon" className="user-card-image"/>
                <div className="user-card-details">
                    <p><span
                        className={disable ? "user-card-title-color-disabled" : "user-card-title-color"}>Category: </span> {couponDetails.category.name}
                    </p>
                    <p><span
                        className={disable ? "user-card-title-color-disabled" : "user-card-title-color"}>Title: </span> {couponDetails.title}
                    </p>
                    {couponDetails.company &&
                        <p><span
                            className={disable ? "user-card-title-color-disabled" : "user-card-title-color"}>Company: </span> {couponDetails.company?.name}
                        </p>
                    }
                    <p><span
                        className={disable ? "user-card-title-color-disabled" : "user-card-title-color"}>Description: </span> {couponDetails.description}
                    </p>
                    {!disable ?
                        <>
                            <p><span
                                className="user-card-title-color">Start date: </span> {formatDateForUser(couponDetails.startDate)}
                            </p>
                            <p><span
                                className="user-card-title-color">End date: </span> {formatDateForUser(couponDetails.endDate)}
                            </p>
                            <p><span className="user-card-title-color">Amount: </span> {couponDetails.amount}</p>
                            <p><span className="user-card-title-color">Price: </span> {couponDetails.price}</p>
                        </> :
                        <span className="user-card-title-color-purchased-message">You’ve previously purchased this coupon</span>
                    }

                </div>
            </div>
        </div>
    );
}

export default CouponCard;
