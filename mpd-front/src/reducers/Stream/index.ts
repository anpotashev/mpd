import {CHANGE_STREAMING, CONNECTION_STATE, STREAM_URL} from "constants/ActionTypes";
import {ILoading} from "reducers";
import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";
import {getBooleanCookie, setCookie} from "../../lib/cookie";

const ENABLE_STREAMING_COOCKIE_NAME = "MPD_STREAMING_ENABLE";

export interface IStreamReducer extends ILoading {
    streamUrl: string;
    enableStreaming: boolean;
}

function initValue(): IStreamReducer {
    return {
        requestStatus: LOADING.notLoading,
        streamUrl: "",
        enableStreaming: getBooleanCookie(ENABLE_STREAMING_COOCKIE_NAME)
    };
}

export default function (state: IStreamReducer = initValue(), action: any): IStreamReducer {

    switch (action.type) {
        case STREAM_URL + SUCCESS_SUFFIX:
            return {
                requestStatus: LOADING.loaded,
                streamUrl: action.payload,
                enableStreaming: state.enableStreaming
            };
        case STREAM_URL + PROCESSING_SUFFIX:
            return {
                requestStatus: LOADING.loading,
                streamUrl:"",
                enableStreaming: state.enableStreaming
            };
        case STREAM_URL + FAILED_SUFFIX:
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return initValue();
        case CHANGE_STREAMING:
            setCookie(ENABLE_STREAMING_COOCKIE_NAME, action.payload.newState, {expires: 10000});
            return Object.assign({} , state, {enableStreaming: action.payload.newState});
    }
    return state;
};
