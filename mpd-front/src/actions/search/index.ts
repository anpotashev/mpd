import {
    SAVE_CONDITION,
    REMOVE_CONDITION,
    RENAME_CONDITION,
    SHOW_NEW_CONDITION_WINDOW,
    START_EDIT, CANCEL_EDIT
} from "constants/ActionTypes";
import {IBaseSearchCondition} from "../../reducers/Search";
import {WS_REQUEST, WsDestination} from "../../constants/Socks";

export const saveSearchCondition = (name: string, condition: IBaseSearchCondition) => {
    console.log(condition);
    return {
        type: SAVE_CONDITION,
        payload: {
            name: name,
            condition: condition
        }
    };
};


export const removeSearchCondition = (name: string) => {
    return {
        type: REMOVE_CONDITION,
        payload: {
            name: name
        }
    };
};


export const renameSearchCondition = (name: string, newName: string) => {
    return {
        type: RENAME_CONDITION,
        payload: {
            name: name,
            newName: newName
        }
    };
};

export const showNewConditionWindow = () => {
    return {
        type: SHOW_NEW_CONDITION_WINDOW
    }
};

export const hideNewConditionWindow = () => {
    return {
        type: SHOW_NEW_CONDITION_WINDOW
    }
};
export const startEdit = () => {
    return {
        type: START_EDIT
    }
};
export const cancelEdit = () => {
    return {
        type: CANCEL_EDIT
    }
};
