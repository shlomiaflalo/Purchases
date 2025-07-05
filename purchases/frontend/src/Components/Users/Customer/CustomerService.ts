import {Customer} from "../../../Models/Customer.ts";

class CustomerService {


    public isFormValid = (customer: Partial<Customer>) => {
        if (!customer) return false;

        return !!(
            customer?.firstName?.trim() &&
            customer?.lastName?.trim() &&
            customer?.email?.trim() &&
            customer?.password?.trim()
        );

    };

    public hasChanges = (customerA: Customer, customerB: Customer) => {
        if (!customerA || !customerB) return false;

        return (
            customerA.firstName.trim() !== customerB.firstName.trim() ||
            customerA.lastName.trim() !== customerB.lastName.trim() ||
            customerA.email.trim() !== customerB.email.trim() ||
            customerA.password.trim() !== customerB.password.trim()
        );
    };
}

const customerService = new CustomerService();
export default customerService;
