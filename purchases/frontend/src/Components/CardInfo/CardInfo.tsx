import customer from "../../assets/Customer.png";
import company from "../../assets/Company.png";
import coupon from "../../assets/Coupon.png";
import admin from "../../assets/CouponMood4.png";

import React from "react";
import "./CardInfo.css"

interface CardInfoProps {
    imgType?: "CUSTOMER" | "ADMINISTRATOR" | "COMPANY" | "COUPON";
    clickable: boolean;
    child?: React.ReactNode;
}

const CardInfo: React.FC<CardInfoProps> = ({imgType, clickable, child}) => {
    return (
        <div className={`card-info ${clickable && `card-cursor`}`}>
            {
                imgType &&
                <img
                    src={imgType === "COMPANY" ? company : imgType === "CUSTOMER"
                        ? customer : imgType === "ADMINISTRATOR" ? admin : coupon}
                    alt={imgType}
                    className="card-info-image"
                />
            }
            {child && <>{child}</>}
        </div>
    );
};
export default CardInfo;