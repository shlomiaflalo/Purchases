import CardInfo from "../CardInfo/CardInfo.tsx";
import React, {useEffect} from "react";
import {RootState, store} from "../../Redux/Store";
import "./MyProfile.css"
import ExpireTimeDisplay from "../../Utils/ExpireTimeDisplay.tsx";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {getOneCompanyApi, getOneCustomerApi} from "../../Services/AdminApi.ts";
import {addCompanyDetails} from "../../Redux/companySlice.ts";
import notificationService from "../NotificationService/NotificationService.tsx";
import {addCustomerDetails} from "../../Redux/customerSlice.ts";


const MyProfile: React.FC = () => {
    const user = store.getState().auth.user;
    const company = useSelector((state: RootState) => state.company.company);
    const customer = useSelector((state: RootState) => state.customer.customer);

    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        if (user.clientType === "COMPANY") {
            if (company.id == 0) {
                getOneCompanyApi(user.id)
                    .then((company) => {
                        dispatch(addCompanyDetails(company));
                    })
                    .catch((err) => {
                        notificationService.errorAxiosApiCall(err);
                    });
            }
        }

    }, [company.id, dispatch, user.clientType, user.id]);

    useEffect(() => {
        if (user.clientType === "CUSTOMER") {
            if (customer.id == 0) {
                getOneCustomerApi(user.id)
                    .then((customer) => {
                        dispatch(addCustomerDetails(customer));
                    })
                    .catch((err) => {
                        notificationService.errorAxiosApiCall(err);
                    });
            }
        }

    }, [customer.id, dispatch, user.clientType, user.id]);


    const navigateToCustomerPage = () => {
        navigate(`/customer/edit-my-profile`);
    };
    const navigateToCompanyPage = () => {
        navigate(`/company/edit-my-profile`);
    };

    const navigateInsideCard = () => {
        if (user.clientType === "COMPANY") {
            navigateToCompanyPage();
        } else if (user.clientType === "CUSTOMER") {
            navigateToCustomerPage();
        }
    }

    const name = () => {
        if (user.clientType === "COMPANY") {
            return company.name;
        } else if (user.clientType === "CUSTOMER") {
            return customer.firstName + " " + customer.lastName;
        } else if (user.clientType === "ADMINISTRATOR") {
            return user.name;
        }
    }

    const email = () => {
        if (user.clientType === "COMPANY") {
            return company.email;
        } else if (user.clientType === "CUSTOMER") {
            return customer.email;
        } else if (user.clientType === "ADMINISTRATOR") {
            return user.email;
        }
    }
    return (
        <div className="my-profile-wrapper">
            <div className="card-profile-container">
                <CardInfo
                    imgType={user.clientType === "COMPANY" ? "COMPANY" : user.clientType === "CUSTOMER"
                        ? "CUSTOMER" : user.clientType === "ADMINISTRATOR" ? "ADMINISTRATOR" : "COUPON"}
                    clickable={false}
                />
            </div>
            <div className="card-profile-container"
                 onClick={navigateInsideCard}>
                <CardInfo
                    child={
                        <>
                            <h1 className="my-profile-header">
                                My Profile
                            </h1>
                            <p><span
                                className="title-color-profile">Client Type: </span>{" " + user.clientType.toLowerCase()}
                            </p>
                            <p><span className="title-color-profile">Name: </span>{" " + name()}</p>
                            <p><span className="title-color-profile">Email: </span>{" " + email()}</p>
                            <p><span className="title-color-profile">You will be logged out automatically if no activity is detected by this time: {" "}</span><ExpireTimeDisplay/>
                            </p>
                        </>
                    }
                    clickable={user.clientType !== "ADMINISTRATOR"}>
                </CardInfo>
            </div>
        </div>
    );
};

export default MyProfile;
