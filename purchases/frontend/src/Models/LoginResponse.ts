export interface LoginResponse {
    token: string,
    id: number,
    email: string,
    name: string,
    clientType: string,
    expireTime: string;
}