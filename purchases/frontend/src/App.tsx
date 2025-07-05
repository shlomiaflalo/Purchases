import './App.css'

import MyRouting from "./Components/Routing/MyRouting.tsx";
import {setNavigator} from "./Utils/navigationService.ts";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

function App() {

    const navigate = useNavigate();

    useEffect(() => {
        setNavigator(navigate);
    }, [navigate]);

    return (
        <div>
            <h1 className="logo">Purchases</h1>
            <MyRouting/>
        </div>
    );
}

export default App;
