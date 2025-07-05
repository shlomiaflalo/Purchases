import {useParams, useNavigate} from "react-router-dom";
import {useState, useEffect} from "react";
import {Company} from "../../../Models/Company.ts";
import notificationService from "../../NotificationService/NotificationService.tsx";
import {RootState, store} from "../../../Redux/Store.ts";
import {deleteCompanyApi, getAllCompaniesApi, getOneCompanyApi, updateCompanyApi} from "../../../Services/AdminApi.ts";
import {setCompanies, updateCompany, deleteCompany} from "../../../Redux/adminSlice.ts";
import {useDispatch, useSelector} from "react-redux";
import "./CompanyDetails.css"
import ModalWindow from "../../ModalWindow/ModalWindow.tsx";
import companyService from "./CompanyService.ts";
import {useForm} from "react-hook-form";
import {addCompanyDetails, updateCompanyDetails} from "../../../Redux/companySlice.ts";

export default function CompanyDetails() {

    const [showConfirm, setShowConfirm] = useState(false);

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {handleSubmit} = useForm<Company>();

    const {id} = useParams<{ id: string }>();
    const numericId = Number(id);

    const companies = useSelector((state: RootState) => state.admin.companies);
    const company = useSelector((state: RootState) => state.company.company);
    const user = store.getState().auth.user;

    const [companyUpdate, setCompanyUpdate] = useState<Company | undefined>(undefined);
    const [companyForModal, setCompanyForModal] = useState<Company | undefined>(undefined);

    useEffect(() => {
        if (user.clientType === "COMPANY") {
            if (company.id == 0) {
                getOneCompanyApi(user.id)
                    .then((company) => {
                        dispatch(addCompanyDetails(company));
                    })
                    .catch((err) => {
                        notificationService.errorAxiosApiCall(err);
                    });
            }
            setCompanyUpdate(company);
            setCompanyForModal(company);
        }

    }, [company, company.id, dispatch, user.clientType, user.id]);

    useEffect(() => {
        const loadCompany = async () => {

            if (user.clientType === "ADMINISTRATOR") {
                if (isNaN(numericId)) {
                    navigate("/404");
                    return;
                }

                try {
                    const allCompanies =
                        companies.length === 0 ? await getAllCompaniesApi() : companies;

                    if (companies.length === 0) {
                        dispatch(setCompanies(allCompanies));
                    }

                    const found = allCompanies.find((c) => c.id === numericId);
                    if (!found) {
                        navigate("/404", {replace: true});
                        return;
                    }

                    setCompanyUpdate(found);
                    setCompanyForModal(found);
                } catch (error) {
                    notificationService.errorAxiosApiCall(error);
                }
            }
        }

        loadCompany();
    }, [numericId]);

    const handleUpdate = async () => {
        if (!companyUpdate) return;

        try {
            const updatedCompany = await updateCompanyApi(companyUpdate);

            if (user.clientType === "ADMINISTRATOR") {
                dispatch(updateCompany(updatedCompany));
                navigate("/administrator/companies");
            } else if (user.clientType === "COMPANY") {
                dispatch(updateCompanyDetails(updatedCompany));
                navigate("/company/my-profile");
            }
            notificationService.successfulLogin(`Successfully updated company: ${updatedCompany.name}`, "company");
        } catch (error) {
            notificationService.errorAxiosApiCall(error);
        }
    };

    const handleDelete = () => {
        if (!companyForModal) return;
        //If a user has clicked the delete button, we pop up a window
        deleteCompanyApi(numericId).then(() => {
            dispatch(deleteCompany(Number(id)));
            notificationService.successfulLogin(`Successfully deleted company: ${companyForModal?.name}`, "company");
            navigate("/administrator/companies");
        }).catch(error => {
            notificationService.errorAxiosApiCall(error)
        });
    };

    if (!companyUpdate || !companyForModal) return null;


    return (
        <>

            <h1 className="company-details-container">{user.clientType === "ADMINISTRATOR" ? "Edit this company" : "Profile Settings"}</h1>
            <form className="company-details" onSubmit={handleSubmit(handleUpdate)}>
                <input
                    className="input-field-company-details"
                    type="text"
                    placeholder="Name"
                    value={companyUpdate.name}
                    onChange={(e) => setCompanyUpdate({...companyUpdate, name: e.target.value})}
                    minLength={2} maxLength={20}
                    required
                    disabled={user.clientType === "COMPANY"}
                />
                <input
                    className="input-field-company-details"
                    type="email"
                    placeholder="Email"
                    value={companyUpdate.email}
                    onChange={(e) => setCompanyUpdate({...companyUpdate, email: e.target.value})}
                    required
                />
                <input
                    className="input-field-company-details"
                    type="password"
                    placeholder="Password"
                    value={companyUpdate.password}
                    onChange={(e) => setCompanyUpdate({...companyUpdate, password: e.target.value})}
                    minLength={8} maxLength={50}
                    required
                />

                <button type="submit"
                        disabled={!companyService.isFormValid(companyUpdate) || !companyService.hasChanges(companyForModal, companyUpdate)}
                >{user.clientType === "ADMINISTRATOR" ? "Update this company" : "Update your settings"}</button>
                {
                    user.clientType === "ADMINISTRATOR" &&
                    <button type="button" onClick={() => setShowConfirm(true)}>Delete this company</button>
                }
            </form>
            {showConfirm && (
                <ModalWindow
                    message={`Are you sure you want to delete ${companyForModal?.name}?`}
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
