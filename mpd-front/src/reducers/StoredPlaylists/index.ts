import {CONNECTION_STATE, STORED_PLAYLISTS} from "constants/ActionTypes";
import {ILoading} from "reducers";
import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";
import {IPlaylistItem} from "../Playlist";

export interface IStoredPlaylistReducer extends ILoading {

    playlists: IStoredPlaylist[];
}

interface IStoredPlaylist {
    name: string;
    playlistItems: IPlaylistItem[]

}

const initValue: IStoredPlaylistReducer = {
    requestStatus: LOADING.notLoading,
    playlists: []
    // items: [] // сам плейлист
};

export default function (state: IStoredPlaylistReducer = initValue, action: any): IStoredPlaylistReducer {

    switch (action.type) {
        case STORED_PLAYLISTS + SUCCESS_SUFFIX:
            return {
                requestStatus: LOADING.loaded,
                playlists: action.payload
            };
        case STORED_PLAYLISTS + PROCESSING_SUFFIX:
            return {
                requestStatus: LOADING.loading,
                playlists: []
            };
        case STORED_PLAYLISTS + FAILED_SUFFIX:
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return initValue;
    }
    return state;
};
