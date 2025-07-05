import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate, useParams} from "react-router-dom";
import {useForm} from "react-hook-form";
import {RootState} from "../../Redux/Store.ts";
import {Coupon} from "../../Models/Coupon.ts";
import {addPurchase, setAllCoupons, setAllPurchases, updateCouponPurchase} from "../../Redux/customerSlice.ts";
import {getAllCouponsApi, getCustomerCouponsApi} from "../../Services/CouponApi.ts";
import notificationService from "../NotificationService/NotificationService.tsx";
import {MyDate} from "../../MyDate/MyDate.tsx";
import ModalWindow from "../ModalWindow/ModalWindow.tsx";
import {purchaseCouponApi} from "../../Services/PurchaseApi.ts";


export default function CouponPurchase() {

    const [showConfirm, setShowConfirm] = useState(false);

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {setValue, watch} = useForm<Coupon>();

    const {id} = useParams<{ id: string }>();
    const numericId = Number(id);

    const allCoupons = useSelector((state: RootState) => state.customer.allCoupons);
    const couponPurchase = useSelector((state: RootState) => state.customer.purchases);

    const [coupon, setCoupon] = useState<Coupon | undefined>(undefined);

    const loadAndSetCoupon = (coupons: Coupon[]) => {
        const found = coupons.find((c) => c.id === numericId);

        if (!found) {
            navigate("/404", {replace: true});
            return;
        }

        setValue("startDate", found.startDate);
        setValue("endDate", found.endDate);
        setCoupon(found);
    };

    useEffect(() => {

        if (isNaN(numericId) || !Number.isInteger(numericId) || !id) {
            navigate("/404");
            return;
        }

        if (couponPurchase.length === 0) {
            getCustomerCouponsApi().then((coupons) => {
                dispatch(setAllPurchases(coupons));
            }).catch((err) => {
                notificationService.errorAxiosApiCall(err);
            });
        }

        if (allCoupons.length === 0) {
            getAllCouponsApi().then((coupons) => {
                dispatch(setAllCoupons(coupons));
                loadAndSetCoupon(coupons);
            }).catch((err) => {
                notificationService.errorAxiosApiCall(err);
            });
        } else {
            loadAndSetCoupon(allCoupons);
        }
    }, [numericId]);

    const isPurchased = () => {
        return couponPurchase.some(c => c.id === coupon?.id);
    };

    const handlePurchase = () => {
        if (!coupon) return;
        purchaseCouponApi(coupon.id!)
            .then(() => {
                setCoupon((prevState) => {
                    prevState = {...coupon, amount: coupon.amount - 1};
                    dispatch(updateCouponPurchase(prevState))
                    dispatch(addPurchase(prevState));
                    return prevState;
                })

                notificationService.successPlainText(`Successfully purchased coupon: ${coupon.title}`);
                navigate("/customer/my-coupons");
            })
            .catch((error) => {
                notificationService.errorAxiosApiCall(error);
                navigate("/customer/my-coupons");
            });
    };


    if (!coupon) return null;

    return (
        <>
            <h1 className="coupon-details-container">Purchase this coupon</h1>
            <form className="coupon-details">
                <div className="wrap-coupon-details">
                    <select
                        defaultValue=""
                        className="input-field-add-coupon-2-row"
                        id="category-select"
                        disabled={true}
                    >
                        <option>{coupon.category.name}</option>
                    </select>

                    <input type="text" placeholder="Title" className="input-field-2-row-coupon-details"
                           value={coupon?.title}
                           disabled={true}
                    />

                </div>
                <div className="wrap-coupon-details">
                    <input type="text" placeholder="Description" className="input-field-2-row-coupon-details"
                           value={coupon?.description}
                           disabled={true}
                    />

                    <input
                        type="number"
                        placeholder="Price"
                        className="input-field-2-row-coupon-details"
                        value={coupon?.price}
                        disabled={true}
                    />
                </div>

                <div className="wrap-coupon-details">
                    <MyDate
                        name="startDate"
                        placeholder="Start date"
                        className="input-field-add-coupon-3-row"
                        required={true}
                        valueOnUpdate={coupon?.startDate}
                        setValue={setValue}
                        watch={watch}
                        disabled={true}
                    />

                    <MyDate
                        name="endDate"
                        placeholder="End date"
                        className="input-field-add-coupon-3-row"
                        required={true}
                        valueOnUpdate={coupon?.endDate}
                        setValue={setValue}
                        watch={watch}
                        disabled={true}
                    />

                    <input
                        type="number"
                        placeholder="Amount"
                        className="input-field-3-row-coupon-details"
                        value={coupon?.amount}
                        required
                        disabled={true}
                    />

                </div>
                <button type="button" onClick={() => setShowConfirm(true)}
                        disabled={isPurchased()}>{isPurchased() ? "You’ve previously purchased this coupon" : "Purchase this coupon"}</button>
            </form>
            {showConfirm && (
                <ModalWindow
                    message={`Are you sure you want to purchase ${coupon?.title}?`}
                    onConfirm={() => {
                        handlePurchase();
                        setShowConfirm(false);
                    }}
                    onCancel={() => setShowConfirm(false)}
                />
            )}
        </>
    );
}
