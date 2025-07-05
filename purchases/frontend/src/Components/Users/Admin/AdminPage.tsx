import AdminDashboard from "./AdminDashboard.tsx";
import {getAllCompaniesApi, getAllCustomersApi} from "../../../Services/AdminApi.ts";
import {useDispatch, useSelector} from "react-redux";
import {setCompanies, setCustomers} from "../../../Redux/adminSlice.ts";
import {useEffect} from "react";
import notificationService from "../../NotificationService/NotificationService.tsx";
import {RootState} from "../../../Redux/Store.ts";

function AdminPage() {

    const dispatch = useDispatch();
    const companies = useSelector((state: RootState) => state.admin.companies);
    const customers = useSelector((state: RootState) => state.admin.customers);

    useEffect(() => {
        if (companies.length < 1) {
            // Fetch companies
            getAllCompaniesApi()
                .then((companies) => {
                    dispatch(setCompanies(companies));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

        if (customers.length < 1) {
            // Fetch customers
            getAllCustomersApi()
                .then((customers) => {
                    dispatch(setCustomers(customers));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [companies.length, customers.length, dispatch]);


    return <>
        <AdminDashboard/>
    </>;
}

export default AdminPage;
