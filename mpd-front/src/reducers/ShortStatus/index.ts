import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "../../redux/SockJSMiddleware2";
import {CONNECTION_STATE, SONG_TIME, STATUS} from "../../constants/ActionTypes";

export interface IShortStatusReducer {
    songPos: number;
    playing: boolean;
    songTime: ISongTime;
}

interface ISongTime {
    current: number;
    full: number;
}
const defaultValue : IShortStatusReducer = {
    songPos: -1,
    playing: false,
    songTime: {current: -1, full: -1}
};


export default function (state: IShortStatusReducer = defaultValue, action: any): IShortStatusReducer {
    switch (action.type) {
        case SONG_TIME + SUCCESS_SUFFIX:
            return action.payload;
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return defaultValue;
        default:
            return state;
    }
}