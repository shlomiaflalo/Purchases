import {useForm} from "react-hook-form";
import {Company} from "../../../Models/Company.ts";
import {addCompanyApi} from "../../../Services/AdminApi.ts";
import notificationService from "../../NotificationService/NotificationService.tsx";
import {useDispatch} from "react-redux";
import {addCompany} from "../../../Redux/adminSlice.ts";
import {useNavigate} from "react-router-dom";
import "./AddCompany.css"
import "../../Form/form.css"
import companyService from "./CompanyService.ts";

function AddCompany() {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const {register, handleSubmit, watch, reset} = useForm<Company>();

    const name = watch("name");
    const email = watch("email");
    const password = watch("password");

    const company = {name, email, password};

    function onSubmit(company: Company) {
        addCompanyApi(company).then((data) => {
            dispatch(addCompany(data));
            notificationService.successPlainText("Company added successfully");
            navigate("/administrator/companies")
        }).catch((err) => {
            reset();
            notificationService.errorAxiosApiCall(err)
        })
    }

    return (
        <>
            <h1 className="adding">Adding a company</h1>
            <form className="add" onSubmit={handleSubmit(onSubmit)}>

                <input type="text" placeholder="Name" className="input-field-add" required {
                    ...register("name")} minLength={2} maxLength={20}/>

                <input type="email" placeholder="Email" className="input-field-add" required {
                    ...register("email")}/>

                <input type="password" placeholder="Password" className="input-field-add" required {
                    ...register("password")} minLength={8} maxLength={50}/>

                <button type="submit" disabled={!companyService.isFormValid(company)}>Add</button>
            </form>
        </>

    )
}

export default AddCompany;