import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import notificationService from "../../NotificationService/NotificationService.tsx";
import {RootState, store} from "../../../Redux/Store.ts";
import {addCategoriesCompany, addCompanyDetails, addCoupons} from "../../../Redux/companySlice.ts";
import CompanyDashboard from "./CompanyDashboard.tsx";
import {getAllCategoriesApi} from "../../../Services/CategoryApi.ts";
import {getOneCompanyApi} from "../../../Services/AdminApi.ts";
import {getCompanyCouponsByCompanyApi} from "../../../Services/CouponApi.ts";

function CompanyPage() {

    const dispatch = useDispatch();
    const coupons = useSelector((state: RootState) => state.company.coupons);
    const categories = useSelector((state: RootState) => state.company.categories);
    const company = useSelector((state: RootState) => state.company.company);
    const userId = store.getState().auth.user.id;

    useEffect(() => {
        if (company.id == 0) {
            // Fetch company details for editing
            getOneCompanyApi(userId)
                .then((company) => {
                    dispatch(addCompanyDetails(company));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [company, dispatch, userId]);

    useEffect(() => {
        if (coupons.length == 0) {
            // Fetch company coupons
            getCompanyCouponsByCompanyApi()
                .then((coupons) => {
                    dispatch(addCoupons(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [coupons.length, dispatch]);

    useEffect(() => {
        if (categories.length === 0) {
            // Fetch categories
            getAllCategoriesApi().then((categories) => {
                dispatch(addCategoriesCompany(categories));
            });
        }
    }, [categories.length, dispatch]);

    return <>
        <CompanyDashboard/>
    </>;
}

export default CompanyPage;
