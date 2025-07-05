import React from "react";
import "./AdminDashboard.css";
import {RootState} from "../../../Redux/Store.ts";
import CardInfo from "../../CardInfo/CardInfo.tsx";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";


const AdminDashboard: React.FC = () => {
    const navigate = useNavigate();
    const navigateToCompanies = () => {
        navigate("/administrator/companies");
    };
    const navigateToCustomers = () => {
        navigate("/administrator/customers");
    };

    const totalCustomers = useSelector((state: RootState) => state.admin.customers.length);
    const totalCompanies = useSelector((state: RootState) => state.admin.companies.length);
    const userName = useSelector((state: RootState) => state.auth.user);

    return (
        <div className="dashboard-wrapper">
            <h1 className="title">
                {userName.name}'s Dashboard | Companies & Customers
            </h1>
            <div className="text-count">
                <h3>Total Companies : {totalCompanies}</h3>
                <h3>Total Customers : {totalCustomers}</h3>
            </div>

            <div className="cards-container">
                <div onClick={navigateToCompanies}>
                    <CardInfo
                        imgType="COMPANY"
                        clickable={true}
                    />
                </div>
                <div onClick={navigateToCustomers}>
                    <CardInfo
                        imgType="CUSTOMER"
                        clickable={true}
                    />
                </div>

            </div>
        </div>
    );
};

export default AdminDashboard;
