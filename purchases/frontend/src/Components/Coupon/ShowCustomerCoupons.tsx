import {RootState} from "../../Redux/Store.ts";
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import notificationService from "../NotificationService/NotificationService.tsx";
import SearchBar from "../SearchBar/SearchBar.tsx";
import {getCustomerCouponsApi} from "../../Services/CouponApi.ts";
import "./ShowCoupons.css"
import CouponCard from "./CouponCard.tsx";
import {setAllPurchases} from "../../Redux/customerSlice.ts";

function ShowCustomerCoupons() {

    const customerPurchases = useSelector((state: RootState) => state.customer.purchases);

    const dispatch = useDispatch();
    const [searchTerm, setSearchTerm] = useState("");
    const [couponsFilter, setCouponsFilter] = useState<number>(customerPurchases.length);
    const [hasFetchCoupons, setHasFetchCoupons] = useState<boolean>(false);

    const filteredCoupons = customerPurchases.filter(coupon =>
        coupon.title.toLowerCase().trim().includes(searchTerm.trim().toLowerCase()) ||
        coupon.category.name.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        coupon.company?.name.trim().toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
        coupon.price <= Number(searchTerm.trim())
    );

    useEffect(() => {
        if (!hasFetchCoupons && customerPurchases.length == 0) {
            // Fetch customer coupons
            setHasFetchCoupons(true);
            getCustomerCouponsApi()
                .then((coupons) => {
                    dispatch(setAllPurchases(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }
    }, [customerPurchases, dispatch, hasFetchCoupons])

    useEffect(() => {
        const currentCount = searchTerm ? filteredCoupons.length : customerPurchases.length;
        setCouponsFilter(currentCount);
    }, [searchTerm, filteredCoupons, customerPurchases]);

    return (
        <>
            {
                customerPurchases.length > 0 ?
                    <>
                        <h1 className="coupons-information-title">Current Purchases - {couponsFilter}</h1>

                        <SearchBar placeholderSearch={"Search by title / category / company name / max price"}
                                   searchValue={searchTerm} setSearchTerm={setSearchTerm}/>

                        <div className="coupons-wrapper">
                            {(searchTerm ? filteredCoupons : customerPurchases).map((coupon) => (
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
                                    clickable={false}
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

export default ShowCustomerCoupons;
