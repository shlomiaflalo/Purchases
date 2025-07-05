import {Company} from "./Company.ts";
import {Category} from "./Category.ts";

export interface Coupon {
    id?: number;
    company?: Company;
    category: Category;
    title: string;
    description: string;
    startDate: Date;
    endDate: Date;
    amount: number;
    price: number;
    image?: string;
}