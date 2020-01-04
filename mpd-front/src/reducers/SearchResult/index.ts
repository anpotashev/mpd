import {
    CLEAR_SEARCH,
    SEARCH, SEARCH_NEW
} from "constants/ActionTypes";

import {SUCCESS_SUFFIX} from "../../redux/SockJSMiddleware2";

export interface IItem {
    file: string,
    time: number,
    artist: string,
    albumArtist: string,
    title: string,
    album: string,
    track: string,
    date: string,
    genre: string,
    path: string
}

export interface ISearchResult  {
    items: IItem[]
    from: number
    size: number
    hasMore: boolean
    totalCount: number
}

const defaultSearchResult: ISearchResult = {
    items: [],
    from: 0,
    size: 0,
    hasMore: false,
    totalCount: 0
};

export default function (state: ISearchResult = defaultSearchResult, action: any): ISearchResult {
    switch (action.type) {
        case SEARCH_NEW + SUCCESS_SUFFIX:
            return action.payload;
        case SEARCH + SUCCESS_SUFFIX:
            return action.payload;
        case CLEAR_SEARCH:
            return defaultSearchResult;
    }
    return state;
}