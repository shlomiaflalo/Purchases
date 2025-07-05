import './Header.css';
import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {RootState, store} from "../../../Redux/Store.ts";
import {useDispatch, useSelector} from "react-redux";
import {clearReduxAndAuthFactory} from "../../../Utils/ClearReduxAndAuth.ts";


function Header() {

    const navigate = useNavigate();
    const user = useSelector((state: RootState) => state.auth.user);
    const company = useSelector((state: RootState) => state.company.company);
    const customer = useSelector((state: RootState) => state.customer.customer);

    const dispatch = useDispatch();

    const [isUserLoggedIn, setUserLoggedIn] = useState<boolean>(user != null);

    useEffect(() => {

        const subscription = store.subscribe(() => {
            if (user.clientType === "ADMINISTRATOR") {
                setUserLoggedIn(user != null);
            } else if (user.clientType === "COMPANY") {
                setUserLoggedIn(company != null);
            } else if (user.clientType === "CUSTOMER") {
                setUserLoggedIn(customer != null);
            }
        });

        return () => subscription();

    }, []);

    function logOut() {
        clearReduxAndAuthFactory(user.clientType, dispatch);
        navigate("/", {replace: true});
    }

    const userName = user?.name || '';

    const [greeting, setGreeting] = useState(getTimeBasedGreeting());

    useEffect(() => {
        const interval = setInterval(() => {
            const newGreeting = getTimeBasedGreeting();
            if (newGreeting !== greeting) {
                setGreeting(newGreeting);
            }
        }, 60000); // check every minute

        return () => clearInterval(interval);
    }, [greeting]);

    return (
        <div className='header'>
            {isUserLoggedIn && (
                <div className='header'>
                    <span className="greeting-title">{greeting} {" "} {
                        user.clientType === "ADMINISTRATOR" ?
                            userName : user.clientType === "COMPANY" ? company.name :
                                customer.firstName + " " + customer.lastName
                    }</span>
                    <button onClick={logOut} className={"log-out-button"}>Log-out</button>
                </div>
            )}

        </div>
    );
}

function getTimeBasedGreeting(): string {
    const hour = new Date().getHours();

    if (hour >= 5 && hour < 12) return "Good morning ☀️";
    if (hour >= 12 && hour < 18) return "Good afternoon 🌤";
    if (hour >= 18 && hour < 24) return "Good evening 🌆";
    return "Good night 🌙";
}

export default Header;
