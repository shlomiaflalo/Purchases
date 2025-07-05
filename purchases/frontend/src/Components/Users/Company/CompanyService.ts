import {Company} from "../../../Models/Company.ts";

class CompanyService {

    public isFormValid = (company: Company) => {
        if (!company) return false;

        return !!(
            company.name?.trim() &&
            company.email?.trim() &&
            company.password?.trim()
        );

    };
    public hasChanges = (companyA: Company, companyB: Company) => {
        if (!companyA || !companyB) return false;

        return (
            companyA.name.trim() !== companyB.name.trim() ||
            companyA.email.trim() !== companyB.email.trim() ||
            companyA.password.trim() !== companyB.password.trim()
        );
    };
}

const companyService = new CompanyService();
export default companyService;
