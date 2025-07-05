import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {LoginResponse} from "../Models/LoginResponse.ts";
import notificationService from "../Components/NotificationService/NotificationService.tsx";

interface AuthState {
    user: LoginResponse;
}

const loadUser = (() => {
    try {
        const user = localStorage.getItem('user');

        if (user) {
            const parsedUser = JSON.parse(user);
            return {
                ...parsedUser,
                expireTime: new Date(parsedUser.expireTime),
            } as LoginResponse;
        }
    } catch {
        notificationService.infoPlainText('Error loading user');
    }
    return {
        token: '',
        id: 0,
        email: '',
        name: '',
        clientType: 'Guest',
        expireTime: new Date().toISOString(),
    } as LoginResponse;
})();


const initialState: AuthState = {
    user: loadUser,
};


const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        loginAuth: (state, action: PayloadAction<LoginResponse>) => {
            state.user = action.payload;
            localStorage.setItem('user', JSON.stringify(action.payload));
        },
        logoutAuth: (state) => {
            state.user = {
                token: '',
                id: 0,
                email: '',
                name: '',
                clientType: 'Guest',
                expireTime: new Date().toISOString(),
            } as LoginResponse;
            if (localStorage.getItem('user')) {
                localStorage.removeItem('user')
            }
            if (localStorage.getItem('company')) {
                localStorage.removeItem('company')
            }
            if (localStorage.getItem('customer')) {
                localStorage.removeItem('customer')
            }
        },
    },
});

export const {loginAuth, logoutAuth} = authSlice.actions;
export default authSlice.reducer;
