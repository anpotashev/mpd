import {ILoading} from 'reducers';
import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";
import {CONNECTION_STATE, OUTPUT} from "constants/ActionTypes";

export interface IOutputsReducer extends ILoading {
    outputs: IOutput[]
}

export interface IOutput {
    id: number;
    name: string;
    enabled: boolean;
}

const defaultValue : IOutputsReducer = {
    requestStatus: LOADING.notLoading,
    outputs: []
};

const loading : IOutputsReducer = {
    requestStatus: LOADING.loading,
    outputs: []
};


export default function (state: IOutputsReducer = defaultValue, action: any): IOutputsReducer {
    switch (action.type) {
        case OUTPUT + SUCCESS_SUFFIX:
            return {
                requestStatus: LOADING.loaded,
                outputs: action.payload
            };
        case OUTPUT + PROCESSING_SUFFIX:
            return loading;
        case OUTPUT + FAILED_SUFFIX:
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return defaultValue;
        default:
            return state;
    }
}