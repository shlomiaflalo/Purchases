import {useDispatch, useSelector} from "react-redux";
import {RootState, store} from "../../../Redux/Store.ts";

import {useEffect} from "react";
import CustomerDashboard from "./CustomerDashboard.tsx";
import {getOneCustomerApi} from "../../../Services/AdminApi.ts";
import {
    addCategoriesCustomer, addCompaniesCustomer,
    addCustomerDetails,
    setAllCoupons,
    setAllPurchases
} from "../../../Redux/customerSlice.ts";
import notificationService from "../../NotificationService/NotificationService.tsx";
import {getAllCategoriesApi} from "../../../Services/CategoryApi.ts";
import {getAllCouponsApi, getCustomerCouponsApi} from "../../../Services/CouponApi.ts";
import {getAllCompaniesApi} from "../../../Services/CompanyApi.ts";


function CustomerPage() {

    const dispatch = useDispatch();

    const customerCoupons = useSelector((state: RootState) => state.customer.purchases);
    const allCoupons = useSelector((state: RootState) => state.customer.allCoupons);
    const categories = useSelector((state: RootState) => state.customer.categories);
    const customer = useSelector((state: RootState) => state.customer.customer);
    const companies = useSelector((state: RootState) => state.customer.companies);
    const userId = store.getState().auth.user.id;

    useEffect(() => {
        if (customer.id == 0) {
            // Fetch customer details for editing
            getOneCustomerApi(userId)
                .then((customer) => {
                    dispatch(addCustomerDetails(customer));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [customer, dispatch, userId]);

    useEffect(() => {
        if (allCoupons.length == 0) {
            // Fetch all coupons
            getAllCouponsApi()
                .then((coupons) => {
                    dispatch(setAllCoupons(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [allCoupons.length, dispatch]);

    useEffect(() => {
        if (customerCoupons.length == 0) {
            // Fetch customer coupons
            getCustomerCouponsApi()
                .then((coupons) => {
                    dispatch(setAllPurchases(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [customerCoupons.length, dispatch]);

    useEffect(() => {
        if (categories.length === 0) {
            // Fetch categories
            getAllCategoriesApi().then((categories) => {
                dispatch(addCategoriesCustomer(categories));
            }).catch((err) => {
                notificationService.errorAxiosApiCall(err);
            });
        }
    }, [categories.length, dispatch]);

    useEffect(() => {
        if (companies.length === 0) {
            // Fetch companies
            getAllCompaniesApi().then((companies) => {
                dispatch(addCompaniesCustomer(companies));
            }).catch((err) => {
                notificationService.errorAxiosApiCall(err);
            });
        }
    }, [companies.length, dispatch]);

    return <>
        <CustomerDashboard/>
    </>;
}

export default CustomerPage;

