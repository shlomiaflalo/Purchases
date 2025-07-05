import {RootState} from "../../Redux/Store.ts";
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import notificationService from "../NotificationService/NotificationService.tsx";
import SearchBar from "../SearchBar/SearchBar.tsx";
import {getCompanyCouponsByCompanyApi} from "../../Services/CouponApi.ts";
import {addCoupons} from "../../Redux/companySlice.ts";
import "./ShowCoupons.css"
import CouponCard from "./CouponCard.tsx";

function ShowCompanyCoupons() {

    const companyCoupons = useSelector((state: RootState) => state.company.coupons);

    const dispatch = useDispatch();
    const [searchTerm, setSearchTerm] = useState("");
    const [couponsFilter, setCouponsFilter] = useState<number>(companyCoupons.length);
    const [hasFetchCoupons, setHasFetchCoupons] = useState<boolean>(false);

    const filteredCoupons = companyCoupons.filter(coupon =>
        coupon.title.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        coupon.category.name.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        coupon.price <= Number(searchTerm.trim())
    );

    useEffect(() => {
        if (!hasFetchCoupons && companyCoupons.length == 0) {
            // Fetch coupons
            setHasFetchCoupons(true);
            getCompanyCouponsByCompanyApi()
                .then((coupons) => {
                    dispatch(addCoupons(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }
    }, [companyCoupons, dispatch, hasFetchCoupons])

    useEffect(() => {
        const currentCount = searchTerm ? filteredCoupons.length : companyCoupons.length;
        setCouponsFilter(currentCount);
    }, [searchTerm, filteredCoupons, companyCoupons]);

    return (
        <>
            {
                companyCoupons.length > 0 ?
                    <>
                        <h1 className="coupons-information-title">Current coupons - {couponsFilter}</h1>

                        <SearchBar placeholderSearch={"Search by title or category or max price"}
                                   searchValue={searchTerm} setSearchTerm={setSearchTerm}/>

                        <div className="coupons-wrapper">
                            {(searchTerm ? filteredCoupons : companyCoupons).map((coupon) => (
                                <CouponCard
                                    key={coupon.id}
                                    id={coupon.id}
                                    category={coupon.category}
                                    title={coupon.title}
                                    description={coupon.description}
                                    startDate={coupon.startDate}
                                    endDate={coupon.endDate}
                                    amount={coupon.amount}
                                    price={coupon.price}
                                    clickable={true}
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

export default ShowCompanyCoupons;
