import "./MenuButtonsForAdmin.css"
import {useNavigate} from "react-router-dom";

function MenuButtonsForAdmin() {

    const navigate = useNavigate();

    const navigateToCompanies = () => {
        navigate("/administrator/companies");
    };
    const navigateHome = () => {
        navigate("/administrator");
    };
    const navigateAddCompany = () => {
        navigate("/administrator/addCompany");
    };
    const navigateAddCustomer = () => {
        navigate("/administrator/addCustomer");
    };
    const navigateToCustomers = () => {
        navigate("/administrator/customers");
    };
    const navigateToMyProfile = () => {
        navigate("/administrator/my-profile");
    };

    return (

        <div className="admin_side_bar_buttons">
            <button onClick={navigateHome}>Home</button>
            <button onClick={navigateAddCompany}>Add company</button>
            <button onClick={navigateToCompanies}>Show companies</button>
            <button onClick={navigateAddCustomer}>Add customer</button>
            <button onClick={navigateToCustomers}>Show customers</button>
            <button onClick={navigateToMyProfile}>My profile</button>
        </div>
    )
}

export default MenuButtonsForAdmin;