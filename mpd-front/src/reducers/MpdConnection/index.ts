import {CONNECTION_STATE} from "constants/ActionTypes";

export default function (state = false, action: any): boolean {
    switch (action.type) {
        case CONNECTION_STATE:
            return action.payload;
    }
    return state;
}