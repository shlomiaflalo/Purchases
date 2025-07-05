import './Sidebar.css';
import {RootState, store} from "../../../Redux/Store.ts";
import MenuButtonsForAdmin from "../../Users/Admin/MenuButtonsForAdmin.tsx";
import MenuButtonsForCompany from "../../Users/Company/MenuButtonsForCompany.tsx";
import MenuButtonsForCustomer from "../../Users/Customer/MenuButtonsForCustomer.tsx";
import {useSelector} from "react-redux";

function Sidebar() {

    const user = store.getState().auth.user;
    const company = useSelector((state: RootState) => state.company.company);
    const customer = useSelector((state: RootState) => state.customer.customer);

    return (
        <aside className="side-bar">
            <h1>
                {
                    user.clientType === "ADMINISTRATOR" ?
                        user.name : user.clientType === "COMPANY" ? company.name :
                            customer.firstName + " " + customer.lastName
                } Actions
            </h1>
            {user.clientType === "ADMINISTRATOR" &&
                <MenuButtonsForAdmin/>
            }
            {user.clientType === "COMPANY" &&
                <MenuButtonsForCompany/>
            }
            {user.clientType === "CUSTOMER" &&
                <MenuButtonsForCustomer/>}
        </aside>
    )
}

export default Sidebar;
