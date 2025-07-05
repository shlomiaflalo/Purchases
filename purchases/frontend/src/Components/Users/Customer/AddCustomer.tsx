import {useForm} from "react-hook-form";
import notificationService from "../../NotificationService/NotificationService.tsx";
import {useDispatch} from "react-redux";
import {useNavigate} from "react-router-dom";
import "./AddCustomer.css"
import "../../Form/form.css"
import {Customer} from "../../../Models/Customer.ts";
import {addCustomerApi} from "../../../Services/AdminApi.ts";
import {addCustomer} from "../../../Redux/adminSlice.ts";
import customerService from "./CustomerService.ts";

function AddCustomer() {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const {register, handleSubmit, watch, reset} = useForm<Customer>();

    const firstName = watch("firstName");
    const lastName = watch("lastName");
    const email = watch("email");
    const password = watch("password");

    const customer = {firstName, lastName, email, password};

    function onSubmit(customer: Customer) {
        addCustomerApi(customer).then((data) => {
            dispatch(addCustomer(data));
            notificationService.successPlainText("Customer added successfully");
            navigate("/administrator/customers")
        }).catch((err) => {
            reset();
            notificationService.errorAxiosApiCall(err)
        })
    }

    return (
        <>
            <h1 className="adding adding-customer">Adding a customer</h1>
            <form className="add" onSubmit={handleSubmit(onSubmit)}>

                <input type="text" placeholder="First name" className="input-field-add" required {
                    ...register("firstName")} minLength={2} maxLength={20}/>

                <input type="text" placeholder="Last name" className="input-field-add" required {
                    ...register("lastName")} minLength={2} maxLength={20}/>

                <input type="email" placeholder="Email" className="input-field-add" required {
                    ...register("email")}/>

                <input type="password" placeholder="Password" className="input-field-add" required {
                    ...register("password")} minLength={8} maxLength={50}/>

                <button type="submit" disabled={!customerService.isFormValid(customer)}>Add</button>
            </form>
        </>

    )
}

export default AddCustomer;