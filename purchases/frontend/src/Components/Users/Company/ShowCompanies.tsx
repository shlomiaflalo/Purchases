import {RootState} from "../../../Redux/Store.ts";
import CompanyCard from "./CompanyCard.tsx";
import "./ShowCompanies.css"
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {getAllCompaniesApi} from "../../../Services/AdminApi.ts";
import {setCompanies} from "../../../Redux/adminSlice.ts";
import notificationService from "../../NotificationService/NotificationService.tsx";
import SearchBar from "../../SearchBar/SearchBar.tsx";
import {addCompaniesCustomer} from "../../../Redux/customerSlice.ts";

function ShowCompanies() {

    const companiesCustomer = useSelector((state: RootState) => state.customer.companies);
    const companiesAdmin = useSelector((state: RootState) => state.admin.companies);
    const userType = useSelector((state: RootState) => state.auth.user.clientType);

    const dispatch = useDispatch();
    const [searchTerm, setSearchTerm] = useState("");
    const [companiesFilter, setCompaniesFilter] = useState<number>(companiesAdmin.length);

    const filteredCompanies = (userType === "ADMINISTRATOR" ? companiesAdmin : companiesCustomer).filter(company =>
        company.name.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        company.email.trim().toLowerCase().includes(searchTerm.trim().toLowerCase())
    );

    useEffect(() => {
        if (userType === "ADMINISTRATOR") {
            if (companiesAdmin.length < 1) {
                // Fetch companies
                getAllCompaniesApi()
                    .then((companies) => {
                        dispatch(setCompanies(companies));
                    })
                    .catch((err) => {
                        notificationService.errorAxiosApiCall(err);
                    });
            }
        }
    }, [companiesAdmin])

    useEffect(() => {
        if (userType === "CUSTOMER") {
            if (companiesCustomer.length < 1) {
                // Fetch companies
                getAllCompaniesApi().then((companies) => {
                    dispatch(addCompaniesCustomer(companies));
                });
            }
        }
    }, [companiesCustomer]);

    useEffect(() => {
        const currentCount = searchTerm ? filteredCompanies.length :
            userType === "ADMINISTRATOR" ? companiesAdmin.length : companiesCustomer.length;
        setCompaniesFilter(currentCount);
    }, [companiesAdmin.length, companiesCustomer.length, filteredCompanies.length, searchTerm, userType]);

    return (
        <>
            {
                companiesAdmin.length || companiesCustomer.length > 0 ?
                    <>
                        <h1 className="companies-information-title">Current Companies - {companiesFilter}</h1>

                        <SearchBar placeholderSearch={"Search by name or email"}
                                   searchValue={searchTerm} setSearchTerm={setSearchTerm}/>

                        <div className="companies-wrapper">
                            {(searchTerm ? filteredCompanies : userType === "ADMINISTRATOR" ? companiesAdmin : companiesCustomer).map((company) => (
                                <CompanyCard
                                    key={company.id}
                                    id={company.id}
                                    name={company.name}
                                    email={company.email}
                                    couponCount={company.couponCount}
                                />
                            ))}
                        </div>
                    </>

                    :
                    <div className={"center-no-companies-found"}>No companies found</div>
            }

        </>

    )
}

export default ShowCompanies;
