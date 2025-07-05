import {useParams, useNavigate} from "react-router-dom";
import {useState, useEffect} from "react";
import notificationService from "../../NotificationService/NotificationService.tsx";
import {RootState, store} from "../../../Redux/Store.ts";
import {
    deleteCustomerApi,
    getAllCustomersApi, getOneCustomerApi,
    updateCustomerApi
} from "../../../Services/AdminApi.ts";
import {
    setCustomers,
    updateCustomer,
    deleteCustomer
} from "../../../Redux/adminSlice.ts";
import {useDispatch, useSelector} from "react-redux";
import "./CustomerDetails.css"
import ModalWindow from "../../ModalWindow/ModalWindow.tsx";
import {Customer} from "../../../Models/Customer.ts";
import customerService from "./CustomerService.ts";
import {useForm} from "react-hook-form";
import {addCustomerDetails, updateCustomerDetails} from "../../../Redux/customerSlice.ts";

export default function CustomerDetails() {

    const [showConfirm, setShowConfirm] = useState(false);

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {handleSubmit} = useForm<Customer>();

    const {id} = useParams<{ id: string }>();
    const numericId = Number(id);

    const customers = useSelector((state: RootState) => state.admin.customers);
    const customer = useSelector((state: RootState) => state.customer.customer);
    const user = store.getState().auth.user;

    const [customerUpdate, setCustomerUpdate] = useState<Customer | undefined>(undefined);
    const [customerForModal, setCustomerForModal] = useState<Customer | undefined>(undefined);

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
            setCustomerUpdate(customer);
            setCustomerForModal(customer);
        }

    }, [customer, customer.id, dispatch, user.clientType, user.id]);


    useEffect(() => {
        const loadCustomer = async () => {
            if (user.clientType === "ADMINISTRATOR") {

                if (isNaN(numericId)) {
                    navigate("/404");
                    return;
                }

                try {
                    const allCustomers =
                        customers.length === 0 ? await getAllCustomersApi() : customers;

                    if (customers.length === 0) {
                        dispatch(setCustomers(allCustomers));
                    }

                    const found = allCustomers.find((c) => c.id === numericId);
                    if (!found) {
                        navigate("/404", {replace: true});
                        return;
                    }

                    setCustomerUpdate(found);
                    setCustomerForModal(found);
                } catch (error) {
                    notificationService.errorAxiosApiCall(error);
                }
            }
        }

        loadCustomer();
    }, [numericId]);

    const handleUpdate = async () => {
        if (!customerUpdate) return;

        try {
            const updatedCustomer = await updateCustomerApi(customerUpdate);
            if (user.clientType === "ADMINISTRATOR") {
                dispatch(updateCustomer(updatedCustomer));
                navigate("/administrator/customers");
            } else if (user.clientType === "CUSTOMER") {
                dispatch(updateCustomerDetails(updatedCustomer));
                navigate("/customer/my-profile");
            }
            notificationService.successfulLogin(`Successfully updated customer: ${updatedCustomer.firstName} ${updatedCustomer.lastName}`, "customer");
        } catch (error) {
            notificationService.errorAxiosApiCall(error);
        }
    };

    const handleDelete = () => {
        if (!customerForModal) return;
        //If a user has clicked the delete button, we pop up a window
        deleteCustomerApi(numericId).then(() => {
            dispatch(deleteCustomer(Number(id)));
            notificationService.successfulLogin(`Successfully deleted customer: ${customerForModal.firstName} ${customerForModal.lastName}`, "customer");
            navigate("/administrator/customers");
        }).catch((error)=>{
            notificationService.errorAxiosApiCall(error);
        })
    };

    if (!customerUpdate || !customerForModal) return null;


    return (
        <>
            <h1 className="customer-details-container">{user.clientType === "ADMINISTRATOR" ? "Edit this customer" : "Profile Settings"}</h1>
            <form className="customer-details" onSubmit={handleSubmit(handleUpdate)}>
                <input
                    className="input-field-customer-details"
                    type="text"
                    placeholder="First name"
                    value={customerUpdate.firstName}
                    onChange={(e) => setCustomerUpdate({...customerUpdate, firstName: e.target.value})}
                    minLength={2} maxLength={20}
                    required
                />
                <input
                    className="input-field-customer-details"
                    type="text"
                    placeholder="Last name"
                    value={customerUpdate.lastName}
                    onChange={(e) => setCustomerUpdate({...customerUpdate, lastName: e.target.value})}
                    minLength={2} maxLength={20}
                    required
                />
                <input
                    className="input-field-customer-details"
                    type="email"
                    placeholder="Email"
                    value={customerUpdate.email}
                    onChange={(e) => setCustomerUpdate({...customerUpdate, email: e.target.value})}
                    required
                />
                <input
                    className="input-field-customer-details"
                    type="password"
                    placeholder="Password"
                    value={customerUpdate.password}
                    onChange={(e) => setCustomerUpdate({...customerUpdate, password: e.target.value})}
                    minLength={8} maxLength={50}
                    required
                />

                <button type="submit"
                        disabled={!customerService.isFormValid(customerUpdate) || !customerService.hasChanges(customerForModal, customerUpdate)}
                >{user.clientType === "ADMINISTRATOR" ? "Update this customer" : "Update your settings"}</button>
                {
                    user.clientType === "ADMINISTRATOR" &&
                    <button type="button" onClick={() => setShowConfirm(true)}>Delete this customer</button>
                }
            </form>
            {showConfirm && (
                <ModalWindow
                    message={`Are you sure you want to delete ${customerForModal?.firstName} ${customerForModal?.lastName}?`}
                    onConfirm={() => {
                        handleDelete();
                        setShowConfirm(false);
                    }}
                    onCancel={() => setShowConfirm(false)}
                />
            )}
        </>
    );
}
