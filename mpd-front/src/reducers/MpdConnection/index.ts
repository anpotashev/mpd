import {CONNECTION_STATE, ON_SOCKS_CONNECTED, ON_SOCKS_DISCONNECTED} from "constants/ActionTypes";

import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";
import {ILoading} from "../index";

export interface IMpdConnection extends ILoading {
    connected: boolean | undefined;
}

const defaultValue: IMpdConnection = {
    requestStatus: LOADING.notLoading,
    connected: undefined
};

export default function (state = defaultValue, action: any): IMpdConnection {
    switch (action.type) {
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return {
                requestStatus: LOADING.loaded,
                connected: action.payload
            };
        case CONNECTION_STATE + FAILED_SUFFIX:
        case ON_SOCKS_DISCONNECTED:
        case ON_SOCKS_CONNECTED:
            return defaultValue;
        case CONNECTION_STATE + PROCESSING_SUFFIX:
            return {requestStatus: LOADING.loading, connected: undefined};
    }
    return state;
}