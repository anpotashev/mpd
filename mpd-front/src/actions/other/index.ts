import {CAPTURE_PATH, CHANGE_STREAMING, CLEAR_ERRORS} from "constants/ActionTypes";
import {ICapturedObject} from "../../reducers/Dnd";
import {
    addFileToCurrentPlaylist, addSearch,
    addStoredPlaylist,
    addToCurrentPlaylist,
    deleteFromPlaylist, moveInPlaylist
} from "../websocket";
import {removeSearchCondition} from "../search";

export const changeStreaming = (newState: boolean) => {
    return {
        type: CHANGE_STREAMING,
        payload: {
            newState
        }
    }
};

export const captureObject = (object: ICapturedObject) => {
    return {
        type: CAPTURE_PATH,
        payload: object
    }
};
export const clearErrors = () => {
    return {
        type: CLEAR_ERRORS
    }
};

export const releaseObject = (object: ICapturedObject, pos?: number) => {
    switch (object.type) {
        case 'file':
            return addFileToCurrentPlaylist(object.path, pos);
        case 'directory':
            return addToCurrentPlaylist(object.path, pos);
        case 'playlist':
            return addStoredPlaylist(object.path, pos);
        case 'search':
            if (pos===-1) {
                return removeSearchCondition(object.path);
            }
            return addSearch(object.searchCondition!, pos);
        case 'pos':
            if (pos===-1) { // Позицию -1 отправляет кнопка trash
                return deleteFromPlaylist(object.pos===undefined? -1 : object.pos); // undefined быть не может.
            }
            return moveInPlaylist(object.pos===undefined? -1 : object.pos, pos===undefined? -1: pos); // undefined быть не может.
        default:
            return {type: 'FAKE_ACTION'}; // надо что-то вернуть
    }
};