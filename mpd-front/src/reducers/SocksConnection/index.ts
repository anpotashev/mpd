import {
    ON_SOCKS_CONNECTED,
    ON_SOCKS_DISCONNECTED
} from "../../constants/ActionTypes";
import {ISocksConnectionState} from "./types";

const notConnected : ISocksConnectionState = {
    connected: false
};

const connected : ISocksConnectionState = {
    connected: true
};

export default function (state = notConnected, action: any): ISocksConnectionState {
    switch (action.type) {
        case ON_SOCKS_DISCONNECTED: //пропалало вебсокетное соединение с бэком
            return notConnected;
        case ON_SOCKS_CONNECTED: //установлено соединение с бэком
            return connected;
    }
    return state;
}