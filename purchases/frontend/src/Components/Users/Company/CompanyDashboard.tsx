import React from "react";
import "./CompanyDashboard.css";
import {RootState} from "../../../Redux/Store.ts";
import CardInfo from "../../CardInfo/CardInfo.tsx";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";


const CompanyDashboard: React.FC = () => {
    const navigate = useNavigate();
    const navigateToMyCoupons = () => {
        navigate("/company/my-coupons");
    };
    const navigateToMyProfile = () => {
        navigate("/company/my-profile");
    };

    const totalCoupons = useSelector((state: RootState) => state.company.coupons.length);
    const userName = useSelector((state: RootState) => state.auth.user);

    return (
        <div className="dashboard-wrapper-company">
            <h1 className="title-company">
                {userName.name}'s Dashboard | Coupons & Info
            </h1>
            <div className="text-count-company">
                <h3>Total Coupons : {totalCoupons}</h3>
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
                        imgType="COMPANY"
                        clickable={true}
                    />
                </div>

            </div>
        </div>
    );
};

export default CompanyDashboard;
