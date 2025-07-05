let navigator: (path: string) => void;

export const setNavigator = (navFn: (path: string) => void) => {
    navigator = navFn;
};

export function navigateTo(path: string) {
    if (navigator) navigator(path);
}