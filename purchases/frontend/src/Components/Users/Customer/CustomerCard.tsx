import customer from "../../../assets/Customer.png";
import {useNavigate} from "react-router-dom";
import "../../../Utils/UserCard.css"

interface CustomerProps {
    id: number | undefined;
    firstName: string;
    lastName: string;
    email: string;
}

function CustomerCard(customerDetails: CustomerProps) {
    const navigate = useNavigate();
    const navigateToCustomerPage = () => {
        navigate(`/administrator/customers/${customerDetails.id}`);
    };

    return (
        <div className="user-card-cursor"
             onClick={navigateToCustomerPage}>
            <div className="user-card">
                <img src={customer} alt="customer" className="user-card-image"/>
                <div className="user-card-details">
                    <p><span className="user-card-title-color">First name: </span> {customerDetails.firstName}</p>
                    <p><span className="user-card-title-color">Last name: </span> {customerDetails.lastName}</p>
                    <p><span className="user-card-title-color">Email: </span> {customerDetails.email}</p>
                </div>
            </div>
        </div>
    );
}

export default CustomerCard;
