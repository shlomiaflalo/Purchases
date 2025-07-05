import React from 'react';
import {useSelector} from 'react-redux';
import {RootState} from "../Redux/Store.ts";

const ExpireTimeDisplay: React.FC = () => {
    const expireTime = useSelector((state: RootState) => state.auth.user?.expireTime);

    if (!expireTime) {
        return (
            <div>
                Expiration date not available.
            </div>
        );
    }

    const date = new Date(expireTime);
    const isValidDate = !isNaN(date.getTime());

    const formatted = isValidDate
        ? new Intl.DateTimeFormat('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false,
        }).format(date)
        : 'Invalid Date';

    return (
        <>
            <span>{formatted}</span>
        </>
    );
};

export default ExpireTimeDisplay;
