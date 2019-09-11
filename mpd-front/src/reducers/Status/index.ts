import {ILoading} from 'reducers';
import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "../../redux/SockJSMiddleware2";
import {CONNECTION_STATE, STATUS} from "../../constants/ActionTypes";

export interface IStatusReducer extends ILoading {
    volume?: boolean;
    repeat?: boolean;
    random?: boolean;
    single?: boolean;
    consume: any | undefined;
    playlist: any | undefined;
    playlistlength: any | undefined;
    xfade: any | undefined;
    state: string | null | undefined;
    song: any | undefined;
    songid: any | undefined;
    bitrate: any | undefined;
    audio: any | undefined;
    nextsong: any | undefined;
    nextsongid: any | undefined;
}

const defaultValue : IStatusReducer = {
    requestStatus: LOADING.notLoading,
    volume: undefined,
    repeat: undefined,
    random: undefined,
    single: undefined,
    consume: null,
    playlist: null,
    playlistlength: null,
    xfade: null,
    state: null,
    song: null,
    songid: null,
    bitrate: null,
    audio: null,
    nextsong: null,
    nextsongid: null
};

const loading : IStatusReducer = {
    requestStatus: LOADING.loading,
    volume: undefined,
    repeat: undefined,
    random: undefined,
    single: undefined,
    consume: null,
    playlist: null,
    playlistlength: null,
    xfade: null,
    state: null,
    song: null,
    songid: null,
    bitrate: null,
    audio: null,
    nextsong: null,
    nextsongid: null
};

export default function (state: IStatusReducer = defaultValue, action: any): IStatusReducer {
    switch (action.type) {
        case STATUS + SUCCESS_SUFFIX:
            return {
                requestStatus: LOADING.loaded,
                volume: action.payload.volume,
                repeat: action.payload.repeat,
                random: action.payload.random,
                single: action.payload.single,
                consume: action.payload.consume,
                playlist: action.payload.playlist,
                playlistlength: action.payload.playlistlength,
                xfade: action.payload.xfade,
                state: action.payload.state,
                song: action.payload.song,
                songid: action.payload.songid,
                bitrate: action.payload.bitrate,
                audio: action.payload.audio,
                nextsong: action.payload.nextsong,
                nextsongid: action.payload.nextsongid,
            };
        case STATUS + PROCESSING_SUFFIX:
            return loading;
        case STATUS + FAILED_SUFFIX:
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return defaultValue;

        default:
            return state;
    }
}