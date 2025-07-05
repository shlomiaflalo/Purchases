import {Route, Routes} from "react-router-dom";
import {Bounce, ToastContainer} from 'react-toastify';
import HomePage from "../HomePage/HomePage.tsx";
import Login from "../Login/Login.tsx";
import Layout from "../LayoutArea/Layout/Layout.tsx";
import AuthorizationPage from "../AuthorizationPage/AuthorizationPage.tsx";
import CompanyPage from "../Users/Company/CompanyPage.tsx";
import CustomerPage from "../Users/Customer/CustomerPage.tsx";
import NotFound from "../NotFound/NotFound.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import ShowCompanies from "../Users/Company/ShowCompanies.tsx";
import MyProfile from "../MyProfile/MyProfile.tsx";
import {useSelector} from "react-redux";
import {RootState} from "../../Redux/Store.ts";
import AddCoupon from "../Coupon/AddCoupon.tsx";
import ShowCompanyCoupons from "../Coupon/ShowCompanyCoupons.tsx";
import CouponDetails from "../Coupon/CouponDetails.tsx";
import CompanyDetails from "../Users/Company/CompanyDetails.tsx";
import CustomerDetails from "../Users/Customer/CustomerDetails.tsx";
import {lazy, Suspense} from 'react';
import ShowCustomerCoupons from "../Coupon/ShowCustomerCoupons.tsx";
import ShowCoupons from "../Coupon/ShowCoupons.tsx";
import CouponPurchase from "../Coupon/CouponPurchase.tsx";

// Lazy imports - the admin components
const AdminPage = lazy(() => import("../Users/Admin/AdminPage.tsx"));
const AddCompany = lazy(() => import("../Users/Company/AddCompany.tsx"));
const AddCustomer = lazy(() => import("../Users/Customer/AddCustomer.tsx"));
const ShowCustomers = lazy(() => import("../Users/Customer/ShowCustomers.tsx"));

export default function MyRouting() {
    const token = useSelector((state: RootState) => state.auth.user.token);

    return (
        <>
            <Routes>
                <Route
                    path="/login"
                    element={
                        <ProtectedRoute allowedRoles={["Guest"]}>
                            <Login/>
                        </ProtectedRoute>
                    }/>
                {/*Avoiding useless re-renders once log-out*/}
                {token &&
                    <Route element={<Layout/>}>
                        <Route
                            path="/administrator"
                            element={
                                <Suspense fallback={null}>
                                    <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                        <AdminPage/>
                                    </ProtectedRoute>
                                </Suspense>
                            }/>
                        <Route
                            path="/administrator/companies"
                            element={
                                <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                    <ShowCompanies/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/administrator/addCompany"
                            element={
                                <Suspense fallback={null}>
                                    <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                        <AddCompany/>
                                    </ProtectedRoute>
                                </Suspense>
                            }/>
                        <Route
                            path="/administrator/addCustomer"
                            element={
                                <Suspense fallback={null}>
                                    <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                        <AddCustomer/>
                                    </ProtectedRoute>
                                </Suspense>
                            }/>
                        <Route
                            path="/administrator/customers"
                            element={
                                <Suspense fallback={null}>
                                    <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                        <ShowCustomers/>
                                    </ProtectedRoute>
                                </Suspense>
                            }/>
                        <Route
                            path="/administrator/my-profile"
                            element={
                                <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                    <MyProfile/>
                                </ProtectedRoute>
                            }/>

                        <Route
                            path="/administrator/companies/:id"
                            element={
                                <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                    <CompanyDetails/>
                                </ProtectedRoute>
                            }/>

                        <Route
                            path="/administrator/customers/:id"
                            element={
                                <ProtectedRoute allowedRoles={["ADMINISTRATOR"]}>
                                    <CustomerDetails/>
                                </ProtectedRoute>
                            }/>

                        <Route
                            path="/company"
                            element={
                                <ProtectedRoute allowedRoles={["COMPANY"]}>
                                    <CompanyPage/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/company/add-coupon"
                            element={
                                <ProtectedRoute allowedRoles={["COMPANY"]}>
                                    <AddCoupon/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/company/my-coupons"
                            element={
                                <ProtectedRoute allowedRoles={["COMPANY"]}>
                                    <ShowCompanyCoupons/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/company/my-profile"
                            element={
                                <ProtectedRoute allowedRoles={["COMPANY"]}>
                                    <MyProfile/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/company/my-coupons/:id"
                            element={
                                <ProtectedRoute allowedRoles={["COMPANY"]}>
                                    <CouponDetails/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/company/edit-my-profile"
                            element={
                                <ProtectedRoute allowedRoles={["COMPANY"]}>
                                    <CompanyDetails/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/customer"
                            element={
                                <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                                    <CustomerPage/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/customer/edit-my-profile"
                            element={
                                <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                                    <CustomerDetails/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/customer/my-profile"
                            element={
                                <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                                    <MyProfile/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/customer/my-coupons"
                            element={
                                <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                                    <ShowCustomerCoupons/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/customer/coupons"
                            element={
                                <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                                    <ShowCoupons/>
                                </ProtectedRoute>
                            }/>
                        <Route
                            path="/customer/coupons/:id"
                            element={
                                <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                                    <CouponPurchase/>
                                </ProtectedRoute>
                            }/>
                    </Route>
                }

                <Route path="/homepage" element={<HomePage/>}/>
                <Route path="/home" element={<HomePage/>}/>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/notAllowed" element={<AuthorizationPage/>}/>
                <Route path="*" element={<NotFound/>}/>
                <Route path="/404" element={<NotFound/>}/>
            </Routes>
            <ToastContainer
                position="top-center"
                autoClose={4000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick={false}
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                transition={Bounce}
                icon={false}
            />
        </>

    );
}
