import React from "react";
import "./SearchBar.css"
import {useSelector} from "react-redux";
import {RootState} from "../../Redux/Store.ts";

interface SearchBarProps {
    placeholderSearch: string;
    searchValue: string;
    setSearchTerm: (value: string) => void;
}

const SearchBar: React.FC<SearchBarProps> = ({placeholderSearch, searchValue, setSearchTerm}) => {
    const userType = useSelector((state: RootState) => state.auth.user.clientType);

    return (
        <div className="search-bar-center">
            <input
                type="text"
                placeholder={placeholderSearch}
                className={userType !== "CUSTOMER" ? "search-bar-width-general search-bar" : "search-bar-width-customer search-bar"}
                value={searchValue}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
        </div>
    )
}

export default SearchBar;