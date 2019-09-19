import {
    SAVE_CONDITION,
    REMOVE_CONDITION,
    RENAME_CONDITION,
    START_EDIT,
    CANCEL_EDIT,
    SEARCH
} from "constants/ActionTypes";
import {SUCCESS_SUFFIX} from "../../redux/SockJSMiddleware2";

export interface IBaseSearchCondition {
}

export type Id3Tag = 'ARTIST' | 'ALBUM' | 'TITLE';
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
}

const emptyConditions = {conditions: [], isEditing: false};

export default function (state: Conditions = emptyConditions, action: any): Conditions {
    let conditions = state.conditions;
    switch (action.type) {
        case SAVE_CONDITION:
            let lookup = conditions.find(value => value.name===action.payload.name);
            if (lookup !== undefined) {
                console.log('found');
                lookup.condition = action.payload.condition;
            } else {
                console.log('not found');
                conditions.push({name: action.payload.name, condition: action.payload.condition});
            }
            let result = {conditions: conditions, isEditing: false};
            return result;
        case REMOVE_CONDITION:
            let index = conditions.findIndex(value => value.name===action.name);
            conditions.splice(index, 1);
            return {conditions: conditions, isEditing: false};
        case RENAME_CONDITION:
            let find = conditions.find(value => value.name===action.name);
            if (find !== undefined)
                find.name = action.newName;
            return {conditions: conditions, isEditing: false};
        case START_EDIT:
            return {conditions: conditions, isEditing: true};
        case CANCEL_EDIT:
            return {conditions: conditions, isEditing: false};
    }
    if (action.type === SEARCH + SUCCESS_SUFFIX) {
        console.log('search result: ', JSON.stringify(action.payload, null, 2));
    }
    return state;
}
/**
 * {
  "conditions": [
    {
      "id3Tag": "ALBUM",
      "operation": "CONTAINS",
      "value": "asdf"
    },
    {
      "id3Tag": "ALBUM",
      "operation": "START_WITH",
      "value": "afdsafdsa"
    }
  ],
  "type": "OR"
}
 {
"type": "NOT",
 "conditions": [
 {
  "conditions": [
    {
      "id3Tag": "ALBUM",
      "operation": "CONTAINS",
      "value": "asdf"
    },
    {
      "id3Tag": "ALBUM",
      "operation": "START_WITH",
      "value": "afdsafdsa"
    }
  ],
  "type": "OR"
}]
 }
 */