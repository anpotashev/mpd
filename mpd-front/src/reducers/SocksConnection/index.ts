import {ON_SOCKS_CONNECTED, ON_SOCKS_DISCONNECTED} from "constants/ActionTypes";


export default function (state = false, action: any): boolean {
    switch (action.type) {
        case ON_SOCKS_DISCONNECTED: //пропалало вебсокетное соединение с бэком
            return false;
        case ON_SOCKS_CONNECTED: //установлено соединение с бэком
            return true;
    }
    return state;
}