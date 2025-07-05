import {configureStore} from '@reduxjs/toolkit';
import authReducer from './authSlice';
import adminReducer from './adminSlice';
import companyReducer from './companySlice';
import customerReducer from './customerSlice';


export const store = configureStore({
    reducer: {
        auth: authReducer,
        admin: adminReducer,
        company: companyReducer,
        customer: customerReducer,
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
