import {AppDispatch} from "../Redux/Store.ts";
import {clearAdminData} from "../Redux/adminSlice.ts";
import {clearCompanyData} from "../Redux/companySlice.ts";
import {clearCustomerData} from "../Redux/customerSlice.ts";
import {logoutAuth} from "../Redux/authSlice.ts";

export function clearReduxAndAuthFactory(clientType: string | undefined, dispatch: AppDispatch) {
    switch (clientType) {
        case "ADMINISTRATOR":
            dispatch(clearAdminData());
            break;
        case "COMPANY":
            dispatch(clearCompanyData());
            break;
        case "CUSTOMER":
            dispatch(clearCustomerData());
            break;
    }
    dispatch(logoutAuth());
}
