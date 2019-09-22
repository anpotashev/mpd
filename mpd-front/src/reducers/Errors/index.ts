import {FAILED_SUFFIX} from "redux/SockJSMiddleware2";
import {CLEAR_ERRORS} from "../../constants/ActionTypes";

interface IError {
    type: string;
    message: string;
}

export interface IErrorReducer {
    errors: IError[]
}

export default function (state: IErrorReducer = {errors: []}, action: any): IErrorReducer {
    let errors = state.errors;
    if (action.type.endsWith(FAILED_SUFFIX)) {
        errors.push({type: action.type, message: action.payload});
        return {errors};
    }
    if (action.type===CLEAR_ERRORS) {
        return {errors: []};
    }
    return state;
}