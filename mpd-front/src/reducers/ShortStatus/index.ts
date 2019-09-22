import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";
import {CONNECTION_STATE, SONG_TIME} from "constants/ActionTypes";
import {ILoading} from "../index";

export interface IShortStatusReducer extends ILoading {
    songPos: number;
    playing: boolean;
    songTime: ISongTime;
}

interface ISongTime {
    current: number;
    full: number;
}
const defaultValue : IShortStatusReducer = {
    requestStatus: LOADING.notLoading,
    songPos: -1,
    playing: false,
    songTime: {current: -1, full: -1}
};


export default function (state: IShortStatusReducer = defaultValue, action: any): IShortStatusReducer {
    switch (action.type) {

        case SONG_TIME + PROCESSING_SUFFIX:
            return Object.assign({}, state, {requestStatus: LOADING.loading});
        case SONG_TIME + FAILED_SUFFIX:
            return defaultValue;
        case SONG_TIME + SUCCESS_SUFFIX:
            return Object.assign({}, action.payload, {requestStatus: LOADING.loaded});
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return defaultValue;
        default:
            return state;
    }
}