import {CONNECTION_STATE, TREE} from "constants/ActionTypes";
import {ILoading} from "reducers";
import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";

export interface ITreeReducer extends ILoading {
    root: ITreeElement;
}

export interface ITreeElement {
    children: ITreeElement[];
    directory ?: string;
    file ?: string;
}

const emptyRoot: ITreeElement = {
    children: [],
    directory: ""
};
const initValue: ITreeReducer = {
    requestStatus: LOADING.notLoading,
    root: emptyRoot
};

export default function (state: ITreeReducer = initValue, action: any): ITreeReducer {

    switch (action.type) {
        case TREE + SUCCESS_SUFFIX:
            return {
                requestStatus: LOADING.loaded,
                root: {directory: "/", children: action.payload.children || [] } //если  коллекция пуста
            };
        case TREE + PROCESSING_SUFFIX:
            return {
                requestStatus: LOADING.loading,
                root: emptyRoot
            };
        case TREE + FAILED_SUFFIX:
        case CONNECTION_STATE + SUCCESS_SUFFIX:
            return initValue;
    }
    return state;
};
