import {
    SAVE_CONDITION,
    REMOVE_CONDITION,
    RENAME_CONDITION,
    START_EDIT,
    CANCEL_EDIT,
    SEARCH
} from "constants/ActionTypes";
import {SUCCESS_SUFFIX} from "../../redux/SockJSMiddleware2";
import {getArrayCookie, setArrayCookie} from "../../lib/cookie";

export interface IBaseSearchCondition {
}

export type Id3Tag = 'ARTIST' | 'ALBUM' | 'TITLE' | 'ALBUM_ARTIST' | 'GENRE';
export type Operation = 'START_WITH' | 'CONTAINS' | 'REGEX';
export type PredicateType = 'AND' | 'OR' | 'NOT';

export interface SearchCondition extends IBaseSearchCondition {
    id3Tag: Id3Tag;
    operation: Operation;
    value: string;
}
export interface PredicateCondition extends IBaseSearchCondition {
    type: PredicateType,
    conditions: (IBaseSearchCondition|null)[]
}

export interface NamedSearchCondition {
    name: string;
    condition: IBaseSearchCondition;
}

export interface Conditions {
    conditions: NamedSearchCondition[];
    isEditing: boolean;
    editCondition?: NamedSearchCondition;
}

const loadFromCookie = () => getArrayCookie("conditions");
const saveInCookie = (conditions: IBaseSearchCondition[]) => setArrayCookie("conditions", conditions, {expires: 10000});

const emptyConditions = loadFromCookie() === undefined ? {conditions: [], isEditing: false} : {conditions: loadFromCookie(), isEditing: false};

export default function (state: Conditions = emptyConditions, action: any): Conditions {
    let conditions = state.conditions;
    switch (action.type) {
        case SAVE_CONDITION:
            let lookup = conditions.find(value => value.name===action.payload.name);
            if (lookup !== undefined) {
                lookup.condition = action.payload.condition;
            } else {
                conditions.push({name: action.payload.name, condition: action.payload.condition});
            }
            let result = {conditions: conditions, isEditing: false};
            saveInCookie(conditions);
            return result;
        case REMOVE_CONDITION:
            let index = conditions.findIndex(value => value.name===action.payload.name);
            conditions.splice(index, 1);
            saveInCookie(conditions);
            return {conditions: conditions, isEditing: false};
        case RENAME_CONDITION:
            let find = conditions.find(value => value.name===action.payload.name);
            if (find !== undefined)
                find.name = action.payload.newName;
            saveInCookie(conditions);
            return {conditions: conditions, isEditing: false};
        case START_EDIT:
            if (action.payload.name !== undefined) {
                let condition4edit = conditions.find(value => value.name===action.payload.name);
                if (condition4edit !== undefined) {
                    return {
                        conditions: conditions,
                        isEditing: true,
                        editCondition: {name: action.payload.name, condition: condition4edit}
                    };
                } else {
                    return state;
                }
            }
            return {conditions: conditions, isEditing: true};
        case CANCEL_EDIT:
            return {conditions: conditions, isEditing: false};
    }
    if (action.type === SEARCH + SUCCESS_SUFFIX) {
        console.log('search result: ', JSON.stringify(action.payload, null, 2));
    }
    return state;
}