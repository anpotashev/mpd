import {CONNECTION_STATE, PLAYLIST} from "constants/ActionTypes";
import {ILoading} from "reducers";
import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";

export interface IPlaylistReducer extends ILoading {
    items: IPlaylistItem[];
}

export interface IPlaylistItem {
    file: string;
    time: number;
    artist: string;
    title: string;
    album: string;
    track: string;
    pos: number;
    id: number;
}

export interface IPlaylist {
    items: IPlaylistItem[];
}
const initValue: IPlaylistReducer = {
    requestStatus: LOADING.notLoading,
    items: [] // сам плейлист
};

export default function (state: IPlaylistReducer = initValue, action: any): IPlaylistReducer {

    switch (action.type) {
        case PLAYLIST + SUCCESS_SUFFIX:
            return {
                requestStatus: LOADING.loaded,
                items: action.payload.playlistItems};
        case PLAYLIST + PROCESSING_SUFFIX:
            return {
                requestStatus: LOADING.loading,
                items: []
            };
        case PLAYLIST + FAILED_SUFFIX:
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return initValue;
    }
    return state;
};
