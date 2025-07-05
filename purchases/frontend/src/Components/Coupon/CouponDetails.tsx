import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate, useParams} from "react-router-dom";
import {RootState} from "../../Redux/Store.ts";
import {Coupon} from "../../Models/Coupon.ts";
import {
    deleteCouponByCompanyApi,
    getCompanyCouponsByCompanyApi,
    updateCouponByCompanyApi
} from "../../Services/CouponApi.ts";
import {addCategoriesCompany, addCoupons, deleteCoupon, updateCoupon} from "../../Redux/companySlice.ts";
import notificationService from "../NotificationService/NotificationService.tsx";
import "./CouponDetails.css"
import ModalWindow from "../ModalWindow/ModalWindow.tsx";
import {getAllCategoriesApi} from "../../Services/CategoryApi.ts";
import {deleteCouponPurchasesByCouponIdApi} from "../../Services/PurchaseApi.ts";
import couponService from "./CouponService.ts";
import {MyDate} from "../../MyDate/MyDate.tsx";
import {useForm} from "react-hook-form";

export default function CouponDetails() {

    const [showConfirm, setShowConfirm] = useState(false);
    const [showConfirmHistoryPurchases, setShowConfirmHistoryPurchases] = useState(false);

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {handleSubmit, setValue, watch} = useForm<Coupon>();

    const {id} = useParams<{ id: string }>();
    const numericId = Number(id);

    const coupons = useSelector((state: RootState) => state.company.coupons);
    const categories = useSelector((state: RootState) => state.company.categories);

    const [couponUpdate, setCouponUpdate] = useState<Coupon | undefined>(undefined);
    const [couponForModal, setCouponForModal] = useState<Coupon | undefined>(undefined);

    useEffect(() => {
        if (categories.length == 0) {
            getAllCategoriesApi().then((categories) => {
                dispatch(addCategoriesCompany(categories))
            }).catch((err) => {
                notificationService.errorAxiosApiCall(err);
            });
        }
    }, [categories.length, dispatch])

    useEffect(() => {
        if (coupons.length == 0) {
            // Fetch company coupons
            getCompanyCouponsByCompanyApi()
                .then((coupons) => {
                    dispatch(addCoupons(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [coupons.length, dispatch]);

    useEffect(() => {
        if (coupons.length == 0) return;

        const loadCoupon = () => {

            if (isNaN(numericId)) {
                navigate("/404");
                return;
            }

            const found = coupons.find((c) => c.id === numericId);
            if (!found) {
                navigate("/404", {replace: true});
                return;
            }

            setValue("startDate", found.startDate);
            setValue("endDate", found.endDate);
            setCouponUpdate(found);
            setCouponForModal(found);
        };

        loadCoupon();
    }, [coupons, navigate, numericId, setValue]);

    // Watch for changes to startDate and endDate
    const startDate = watch("startDate");
    const endDate = watch("endDate");

    // Update couponUpdate when startDate or endDate changes
    useEffect(() => {
        if (couponUpdate) {
            setCouponUpdate((prev) => {
                if (!prev) return prev; // If prev is undefined, return undefined

                return {
                    ...prev,
                    startDate,
                    endDate
                };
            });
        }
    }, [startDate, endDate]);

    const handleUpdate = () => {
        if (!couponUpdate) return;

        const coupon = {...couponUpdate, startDate, endDate};
        updateCouponByCompanyApi(coupon).then((updatedCoupon) => {
            navigate("/company/my-coupons");
            dispatch(updateCoupon(updatedCoupon));
            notificationService.successPlainText(`Successfully updated coupon: ${couponUpdate.title}`);
        }).catch((error) => {
            notificationService.errorAxiosApiCall(error);
        });
    };

    const handleDelete = () => {
        if (!couponForModal) return;

        navigate("/company/my-coupons");

        //If a user has clicked the delete button, we pop up a window
        deleteCouponByCompanyApi(numericId).then(() => {
            notificationService.successPlainText(`Successfully deleted coupon: ${couponForModal?.title}`);
            dispatch(deleteCoupon(numericId));
        }).catch(error => {
            notificationService.errorAxiosApiCall(error)
        });
    };

    const handleHistoryPurchasesRemoval = () => {
        if (!couponForModal) return;
        //If a user has clicked the delete purchases button, we pop up a window
        deleteCouponPurchasesByCouponIdApi(numericId).then(() => {
            navigate("/company/my-coupons");
            notificationService.successPlainText(`Successfully deleted purchases history for: ${couponForModal?.title}`);
        }).catch(error => {
            notificationService.errorAxiosApiCall(error)
        });
    };

    if (!couponUpdate || !couponForModal) return null;

    return (
        <>
            <h1 className="coupon-details-container">Edit this coupon</h1>
            <form className="coupon-details" onSubmit={handleSubmit(handleUpdate)}>

                <div className="wrap-coupon-details">
                    <select
                        defaultValue=""
                        className="input-field-add-coupon-2-row"
                        id="category-select"
                        onChange={(e) => {
                            const selected = categories.find(c => c.name === e.target.value);
                            if (selected) {
                                const coupon = {...couponUpdate, category: selected!};
                                setCouponUpdate(coupon);
                            } else {
                                const coupon = {...couponUpdate, category: {id: 0, name: ""}};
                                setCouponUpdate(coupon);
                            }
                        }}
                    >
                        <option value={couponUpdate.category.name}>{couponUpdate.category.name}</option>
                        {categories.filter(c => c.name !== couponUpdate.category.name).map((category) => (
                            <option key={category.id} value={category.name}>
                                {category.name}
                            </option>
                        ))}
                    </select>

                    <input type="text" placeholder="Title" className="input-field-2-row-coupon-details"
                           value={couponUpdate?.title}
                           required
                           onChange={(e) => setCouponUpdate({...couponUpdate, title: e.target.value})}
                           minLength={4} maxLength={20}/>

                </div>
                <div className="wrap-coupon-details">
                    <input type="text" placeholder="Description" className="input-field-2-row-coupon-details"
                           value={couponUpdate?.description}
                           required
                           onChange={(e) => setCouponUpdate({...couponUpdate, description: e.target.value})}
                           minLength={3} maxLength={100}/>

                    <input
                        type="number"
                        placeholder="Price"
                        className="input-field-2-row-coupon-details"
                        min={1}
                        max={50000}
                        value={couponUpdate.price! < 1 ? "" : couponUpdate.price}
                        required
                        onChange={(e) => {
                            // Validate the input value before updating
                            const value = e.target.value;
                            if (!couponService.validNumberInput(value)) return;

                            // Update the state if validation passes
                            setCouponUpdate({...couponUpdate, price: Number(value)});
                        }}
                        onKeyDown={(e) => {
                            couponService.validLogicValueNumber(e);
                        }}
                    />
                </div>

                <div className="wrap-coupon-details">
                    <MyDate
                        name="startDate"
                        placeholder="Start date"
                        className="input-field-add-coupon-3-row"
                        required={true}
                        valueOnUpdate={couponUpdate?.startDate}
                        setValue={setValue}
                        watch={watch}
                    />

                    <MyDate
                        name="endDate"
                        placeholder="End date"
                        className="input-field-add-coupon-3-row"
                        required={true}
                        valueOnUpdate={couponUpdate?.endDate}
                        setValue={setValue}
                        watch={watch}
                    />

                    <input
                        type="number"
                        placeholder="Amount"
                        className="input-field-3-row-coupon-details"
                        min={1}
                        max={1000}
                        value={couponUpdate.amount! < 1 ? "" : couponUpdate.amount}
                        required
                        onChange={(e) => {
                            // Validate the input value before updating
                            const value = e.target.value;
                            if (!couponService.validNumberInput(value)) return;

                            // Update the state if validation passes
                            setCouponUpdate({...couponUpdate, amount: Number(value)});
                        }}
                        onKeyDown={(e) => {
                            couponService.validLogicValueNumber(e);
                        }}
                    />

                </div>

                <button type="submit"
                        disabled={!couponService.isFormValid(couponUpdate) || !couponService.hasChanges(couponForModal, couponUpdate)}>Update
                    this coupon
                </button>
                <button type="button" onClick={() => setShowConfirmHistoryPurchases(true)}>Delete purchases history
                </button>
                <button type="button" onClick={() => setShowConfirm(true)}>Delete this coupon</button>
            </form>
            {showConfirm && (
                <ModalWindow
                    message={`Are you sure you want to delete ${couponForModal?.title}?`}
                    onConfirm={() => {
                        handleDelete();
                        setShowConfirm(false);
                    }}
                    onCancel={() => setShowConfirm(false)}
                />
            )}
            {showConfirmHistoryPurchases && (
                <ModalWindow
                    message={`Are you sure you want to delete purchases for ${couponForModal?.title}?`}
                    onConfirm={() => {
                        handleHistoryPurchasesRemoval();
                        setShowConfirmHistoryPurchases(false);
                    }}
                    onCancel={() => setShowConfirmHistoryPurchases(false)}
                />
            )}
        </>
    );
}
