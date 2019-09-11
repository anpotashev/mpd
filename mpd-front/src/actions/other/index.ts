import {CHANGE_STREAMING} from "constants/ActionTypes";

export const changeStreaming = (newState: boolean) => {
    return {
        type: CHANGE_STREAMING,
        payload: {
            newState
        }
    }
};