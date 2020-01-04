import {
    SAVE_CONDITION,
    REMOVE_CONDITION,
    RENAME_CONDITION,
    SHOW_NEW_CONDITION_WINDOW,
    START_EDIT, CANCEL_EDIT, ADD_SEARCH_BY_NAME, CLEAR_SEARCH
} from "constants/ActionTypes";
import {IBaseSearchCondition} from "reducers/Search";

export const saveSearchCondition = (name: string, condition: IBaseSearchCondition) => {
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
export const startEdit = (name?: string) => {
    return {
        type: START_EDIT,
        payload: {
            name: name
        }
    }
};
export const cancelEdit = () => {
    return {
        type: CANCEL_EDIT
    }
};

export const addSearchByName = (name: string, pos?: number) => {
    return {
        type: ADD_SEARCH_BY_NAME,
        payload: {
            name: name,
            pos: pos
        }
    }
};
export const clearSearch = () => {
    return {
        type: CLEAR_SEARCH
    }
};
