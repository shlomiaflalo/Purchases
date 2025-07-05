import company from "../../../assets/Company.png";
import "../../../Utils/UserCard.css";
import {useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";
import {RootState} from "../../../Redux/Store.ts";

interface CompanyProps {
    id: number | undefined;
    name: string;
    email: string;
    couponCount: number | undefined;
}

function CompanyCard(companyDetails: CompanyProps) {
    const clientType = useSelector((state: RootState) => state.auth.user.clientType);

    const navigate = useNavigate();

    const navigating = () => {
        if (clientType === "ADMINISTRATOR") {
            navigate(`/administrator/companies/${companyDetails.id}`);
        }
        if (clientType == "CUSTOMER") {
            navigate(`/customer/companies/${companyDetails.id}/coupons`);
        }

    };

    return (
        <div className="user-card-cursor"
             onClick={navigating}>
            <div className="user-card">
                <img src={company} alt="company" className="user-card-image"/>
                <div className="user-card-details">
                    <p><span className="user-card-title-color">Name: </span> {companyDetails.name}</p>
                    <p><span className="user-card-title-color">Email: </span> {companyDetails.email}</p>
                    {companyDetails.couponCount !== undefined && (
                        <p><span className="user-card-title-color">Coupons:
                        </span> {companyDetails.couponCount}</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default CompanyCard;
