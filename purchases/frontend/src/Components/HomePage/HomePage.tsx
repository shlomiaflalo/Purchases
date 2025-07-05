import React, {useEffect, useState} from 'react';
import homePagePhoto from "../../assets/CouponMood4.png";
import "./HomePage.css"
import {useNavigate} from "react-router-dom";
import {store} from "../../Redux/Store.ts";
import {LoginResponse} from "../../Models/LoginResponse.ts";

const HomePage: React.FC = () => {

    const navigate = useNavigate();
    const [user, setUser] = useState<LoginResponse>(store.getState().auth.user);
    useEffect(() => {
        const unsubscribe = store.subscribe(() => {
            setUser(store.getState().auth.user);
        });
        return () => {
            unsubscribe();
        }
    }, [])

    const navigateToDashboard = () => {
        if (!user || !user.clientType) {
            navigate("/login");
            return;
        }

        switch (user.clientType) {
            case "ADMINISTRATOR":
                navigate("/administrator");
                break;
            case "COMPANY":
                navigate("/company");
                break;
            case "CUSTOMER":
                navigate("/customer");
                break;
            default:
                navigate("/login");
        }
    };

    return (
        <div className="home-container">
            <img src={homePagePhoto} alt="Home page"/>
            <div className="home-text">
                <h1 className={"home-title"}>Purchases is the place where you can have millions of coupons in your
                    pocket</h1>
                <h2>Connect as a customer to purchase / Company to publish coupons / Admin to manage</h2>
                <h3>Connect & Made your wife happy</h3>
                <h2>that simple.</h2>
                {
                    user.name ?
                        <button onClick={navigateToDashboard} className={"button-connect"}>Go back {user.name}</button>
                        :
                        <button onClick={navigateToDashboard} className={"button-connect"}>Connect Now</button>
                }

            </div>

        </div>
    );
};

export default HomePage;
