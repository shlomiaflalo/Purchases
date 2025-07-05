import {Coupon} from "./Coupon.ts";
import {Customer} from "./Customer.ts";

export interface Purchase {
    id?: number;
    customer: Customer;
    coupon: Coupon;
}