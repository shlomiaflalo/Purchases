import {RootState} from "../../Redux/Store.ts";
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import notificationService from "../NotificationService/NotificationService.tsx";
import SearchBar from "../SearchBar/SearchBar.tsx";
import {getAllCouponsApi, getCustomerCouponsApi} from "../../Services/CouponApi.ts";
import "./ShowCoupons.css"
import CouponCard from "./CouponCard.tsx";
import {setAllCoupons, setAllPurchases} from "../../Redux/customerSlice.ts";

function ShowCoupons() {

    const coupons = useSelector((state: RootState) => state.customer.allCoupons);
    const couponPurchases = useSelector((state: RootState) => state.customer.purchases);

    const dispatch = useDispatch();
    const [searchTerm, setSearchTerm] = useState("");
    const [couponsFilter, setCouponsFilter] = useState<number>(coupons.length);
    const [hasFetchCoupons, setHasFetchCoupons] = useState<boolean>(false);
    const [hasFetchPurchases, setHasFetchPurchases] = useState<boolean>(false);

    const filteredCoupons = coupons.filter(coupon =>
        coupon.title.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        coupon.category.name.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        coupon.company?.name.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        coupon.price <= Number(searchTerm.trim())
    );

    useEffect(() => {
        if (!hasFetchPurchases && couponPurchases.length == 0) {
            // Fetch customer coupons
            setHasFetchPurchases(true);
            getCustomerCouponsApi()
                .then((coupons) => {
                    dispatch(setAllPurchases(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }
    }, [couponPurchases.length, dispatch, hasFetchPurchases])

    useEffect(() => {
        if (!hasFetchCoupons && coupons.length == 0) {
            // Fetch coupons
            setHasFetchCoupons(true);
            getAllCouponsApi()
                .then((coupons) => {
                    dispatch(setAllCoupons(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }
    }, [coupons, dispatch, hasFetchCoupons])

    useEffect(() => {
        const currentCount = searchTerm ? filteredCoupons.length : coupons.length;
        setCouponsFilter(currentCount);
    }, [searchTerm, filteredCoupons, coupons]);


    function isPurchased(couponId: number) {
        return couponPurchases.some(p => p.id === couponId);
    }

    return (
        <>
            {
                coupons.length > 0 ?
                    <>
                        <h1 className="coupons-information-title">Current coupons - {couponsFilter}</h1>

                        <SearchBar placeholderSearch={"Search by title / category / company name / max price"}
                                   searchValue={searchTerm} setSearchTerm={setSearchTerm}/>

                        <div className="coupons-wrapper">
                            {(searchTerm ? filteredCoupons : coupons).map((coupon) => (
                                <CouponCard
                                    key={coupon.id}
                                    id={coupon.id}
                                    category={coupon.category}
                                    title={coupon.title}
                                    company={coupon.company}
                                    description={coupon.description}
                                    startDate={coupon.startDate}
                                    endDate={coupon.endDate}
                                    amount={coupon.amount}
                                    price={coupon.price}
                                    clickable={true}
                                    disabled={isPurchased(coupon.id!)}
                                />
                            ))}
                        </div>
                    </>

                    :
                    <div className={"center-no-coupons-found"}>No coupons found</div>
            }

        </>

    )
}

export default ShowCoupons;
