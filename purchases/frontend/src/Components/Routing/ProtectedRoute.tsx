import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {ReactNode, useEffect} from "react";
import {RootState} from "../../Redux/Store.ts";

interface ProtectedRouteProps {
    allowedRoles: string[];
    children: ReactNode;
}

const ProtectedRoute = ({allowedRoles, children}: ProtectedRouteProps) => {
    const navigate = useNavigate();
    const user = useSelector((state: RootState) => state.auth.user);

    useEffect(() => {
        const isAllowed = allowedRoles.includes(user.clientType);
        if (!isAllowed) {
            sessionStorage.setItem("fromApp", "true");
            navigate("/notAllowed")
            return;
        }
    }, []);

    return <>{children}</>;
};

export default ProtectedRoute;
