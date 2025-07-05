class Config {

}

class DevConfig extends Config {
    apiAddress: string = "http://localhost:4040/api/";
}

class ProdConfig extends Config {
    apiAddress: string = "" // production.
}

const appConfig = process.env.NODE_ENV === "development" ? new DevConfig() : new ProdConfig();
export default appConfig;