import "./Layout.css"
import Header from "../Header/Header.tsx";
import {Outlet} from "react-router-dom";
import Sidebar from "../Sidebar/Sidebar.tsx";

function Layout() {
    return (
        <div className="layout">
            <Header/>
            <div className="main">
                <div className="sidebar">
                    <Sidebar/>
                </div>
                <div className="content">
                    <Outlet/>
                </div>
            </div>
        </div>
    );
}

export default Layout;
