import {useForm} from "react-hook-form";
import notificationService from "../NotificationService/NotificationService.tsx";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import "./AddCoupon.css";
import "../Form/form.css";
import {Coupon} from "../../Models/Coupon.ts";
import {addCouponByCompanyApi, getCompanyCouponsByCompanyApi} from "../../Services/CouponApi.ts";
import {addCategoriesCompany, addCoupon, addCoupons} from "../../Redux/companySlice.ts";
import {useEffect, useState} from "react";
import {getAllCategoriesApi} from "../../Services/CategoryApi.ts";
import {MyDate} from "../../MyDate/MyDate.tsx";
import {RootState} from "../../Redux/Store.ts";
import couponService from "./CouponService.ts";

function AddCoupon() {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const categories = useSelector((state: RootState) => state.company.categories);
    const coupons = useSelector((state: RootState) => state.company.coupons);

    const [fetchCoupon, setFetchCoupon] = useState<Partial<Coupon>>({
        title: "",
        description: "",
        price: 0,
        amount: 0,
        category: {id: 0, name: ""},
        startDate: new Date(),
        endDate: new Date(),
    });

    const {handleSubmit, setValue, watch, reset, register} = useForm<Coupon>();

    useEffect(() => {
        if (categories.length === 0) {
            getAllCategoriesApi().then((categories) => {
                dispatch(addCategoriesCompany(categories));
            }).catch((err) => {
                notificationService.errorAxiosApiCall(err);
            });
        }
    }, [categories.length, dispatch]);

    useEffect(() => {
        if (coupons.length == 0) {
            // Fetch company coupons if rendered
            getCompanyCouponsByCompanyApi()
                .then((coupons) => {
                    dispatch(addCoupons(coupons));
                })
                .catch((err) => {
                    notificationService.errorAxiosApiCall(err);
                });
        }

    }, [coupons.length, dispatch]);

    // Watch for changes to startDate and endDate
    const startDate = watch("startDate");
    const endDate = watch("endDate");

    // Update couponUpdate when startDate or endDate changes
    useEffect(() => {
        if (fetchCoupon) {
            setFetchCoupon((prev) => {
                if (!prev) return prev; // If prev is undefined, return undefined

                return {
                    ...prev,
                    startDate,
                    endDate
                };
            });
        }
    }, [startDate, endDate]);

    function onSubmit() {

        const selectedCategory = categories.find(c => c.name === fetchCoupon.category?.name);
        const newCoupon = {...fetchCoupon, category: selectedCategory!} as Coupon;

        addCouponByCompanyApi(newCoupon).then((data) => {
            dispatch(addCoupon(data));
            notificationService.successPlainText("Coupon added successfully");
            navigate("/company/my-coupons");
        }).catch((err) => {
            notificationService.errorAxiosApiCall(err);
            reset();
        });
    }


    return (
        <>
            <h1 className="adding">Adding a coupon</h1>
            <form className="add" onSubmit={handleSubmit(onSubmit)}>

                <div className="wrap-add-coupon">
                    <select
                        defaultValue=""
                        className="input-field-add-coupon-2-row"
                        id="category-select"
                        onChange={(e) => {
                            const selected = categories.find(c => c.name === e.target.value);
                            if (selected) {
                                const coupon = {...fetchCoupon, category: selected!};
                                setFetchCoupon(coupon);
                            } else {
                                const coupon = {...fetchCoupon, category: {id: 0, name: ""}};
                                setFetchCoupon(coupon);
                            }
                        }}
                    >
                        <option value="">Choose a Category</option>
                        {categories.length > 0 ? categories.map((category) => (
                            <option key={category.id} value={category.name}>
                                {category.name}
                            </option>
                        )) : <option disabled>No category found</option>}
                    </select>
                    <input type="hidden"/>

                    <input
                        type="text"
                        placeholder="Title"
                        className="input-field-add-coupon-2-row"
                        minLength={4}
                        maxLength={20}
                        required
                        value={fetchCoupon.title}
                        onChange={(e) => setFetchCoupon({...fetchCoupon, title: e.target.value})}
                    />

                </div>

                <div className="wrap-add-coupon">
                    <input
                        type="text"
                        placeholder="Description"
                        className="input-field-add-coupon-2-row"
                        minLength={3}
                        maxLength={100}
                        required
                        value={fetchCoupon.description}
                        onChange={(e) => setFetchCoupon({...fetchCoupon, description: e.target.value})}
                    />

                    <input
                        type="number"
                        placeholder="Price"
                        className="input-field-add-coupon-2-row"
                        min={1}
                        max={50000}
                        required
                        value={fetchCoupon.price! < 1 ? "" : fetchCoupon.price}
                        onChange={(e) => {
                            // Validate the input value before updating
                            const value = e.target.value;
                            if (!couponService.validNumberInput(value)) return;

                            // Update the state if validation passes
                            setFetchCoupon({...fetchCoupon, price: Number(value)});
                        }}
                        onKeyDown={(e) => {
                            couponService.validLogicValueNumber(e);
                        }}
                    />
                </div>

                <div className="wrap-add-coupon">
                    <MyDate
                        name="startDate"
                        setValue={setValue}
                        watch={watch}
                        placeholder="Start date"
                        className="input-field-add-coupon-3-row"
                        required={true}
                    />
                    <input type="hidden" {...register("startDate", {required: true})} />

                    <MyDate
                        name="endDate"
                        setValue={setValue}
                        watch={watch}
                        placeholder="End date"
                        className="input-field-add-coupon-3-row"
                        required={true}
                    />
                    <input type="hidden" {...register("endDate", {required: true})} />

                    <input
                        type="number"
                        placeholder="Amount"
                        className="input-field-add-coupon-3-row"
                        min={1}
                        max={1000}
                        required
                        value={fetchCoupon.amount! < 1 ? "" : fetchCoupon.amount}
                        onChange={(e) => {
                            // Validate the input value before updating
                            const value = e.target.value;
                            if (!couponService.validNumberInput(value)) return;

                            // Update the state if validation passes
                            setFetchCoupon({...fetchCoupon, amount: Number(value)});
                        }}
                        onKeyDown={(e) => {
                            couponService.validLogicValueNumber(e);
                        }}
                    />
                </div>
                <button type="submit" className="add-button" disabled={!couponService.isFormValid(fetchCoupon)}>Add
                </button>
            </form>
        </>
    );
}

export default AddCoupon;
