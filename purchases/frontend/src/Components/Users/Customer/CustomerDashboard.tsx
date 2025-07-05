import React from "react";
import "./CustomerDashboard.css";
import {RootState} from "../../../Redux/Store.ts";
import CardInfo from "../../CardInfo/CardInfo.tsx";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";


const CustomerDashboard: React.FC = () => {
    const navigate = useNavigate();
    const navigateToMyCoupons = () => {
        navigate("/customer/my-coupons");
    };
    const navigateToMyProfile = () => {
        navigate("/customer/my-profile");
    };

    const totalCoupons = useSelector((state: RootState) => state.customer.purchases.length);
    const userName = useSelector((state: RootState) => state.auth.user);

    return (
        <div className="dashboard-wrapper-customer">
            <h1 className="title-customer">
                {userName.name}'s Dashboard | Coupons & Info
            </h1>
            <div className="text-count-customer">
                <h3>My Purchases : {totalCoupons}</h3>
                <h3>My Profile</h3>
            </div>

            <div className="cards-container">
                <div onClick={navigateToMyCoupons}>
                    <CardInfo
                        imgType="COUPON"
                        clickable={true}
                    />
                </div>
                <div onClick={navigateToMyProfile}>
                    <CardInfo
                        imgType="CUSTOMER"
                        clickable={true}
                    />
                </div>

            </div>
        </div>
    );
};

export default CustomerDashboard;
