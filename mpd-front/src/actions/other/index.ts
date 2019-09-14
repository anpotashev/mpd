import {CAPTURE_PATH, CHANGE_STREAMING} from "constants/ActionTypes";
import {CapturedObjectType, ICapturedObject} from "../../reducers/Dnd";
import {addFileToCurrentPlaylist, addStoredPlaylist, addToCurrentPlaylist} from "../websocket";
import {object} from "prop-types";

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
        payload: {
            path: object.path,
            type: object.type
        }

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
        default:
            return {type: 'FAKE_ACTION'};
    }
};