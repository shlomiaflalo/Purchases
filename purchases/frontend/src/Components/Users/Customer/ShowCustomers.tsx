import {RootState} from "../../../Redux/Store.ts";
import "./ShowCustomers.css"
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {getAllCustomersApi} from "../../../Services/AdminApi.ts";
import {setCustomers} from "../../../Redux/adminSlice.ts";
import notificationService from "../../NotificationService/NotificationService.tsx";
import CustomerCard from "./CustomerCard.tsx";
import SearchBar from "../../SearchBar/SearchBar.tsx";

function ShowCustomers() {
    const customers = useSelector((state: RootState) => state.admin.customers);
    const dispatch = useDispatch();

    const [searchTerm, setSearchTerm] = useState("");
    const [customersFilter, setCustomersFilter] = useState<number>(customers.length);

    const filteredCustomers = customers.filter(customer =>
        customer.firstName.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        customer.lastName.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        customer.email.trim().toLowerCase().includes(searchTerm.trim().toLowerCase())
    );

    useEffect(() => {
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
    }, [customers])

    useEffect(() => {
        const currentCount = searchTerm ? filteredCustomers.length : customers.length;
        setCustomersFilter(currentCount);
    }, [searchTerm, filteredCustomers, customers]);

    return (
        <>
            {
                customers.length > 0 ?
                    <>
                        <h1 className="customers-information-title">Current Customers - {customersFilter}</h1>
                        <SearchBar placeholderSearch={"Search by name or email"}
                                   searchValue={searchTerm} setSearchTerm={setSearchTerm}/>

                        <div className="customers-wrapper">
                            {
                                (searchTerm ? filteredCustomers : customers).map((customer) => (
                                    <CustomerCard
                                        key={customer.id}
                                        id={customer.id}
                                        firstName={customer.firstName}
                                        lastName={customer.lastName}
                                        email={customer.email}
                                    />
                                ))
                            }
                        </div>
                    </> :
                    <div className={"center-no-customers-found"}>No customers found</div>
            }

        </>

    )
}

export default ShowCustomers;
