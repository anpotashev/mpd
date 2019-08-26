import {
    ON_MESSAGE_RECEIVED,
    ON_SOCKS_CONNECTED,
    ON_SOCKS_DISCONNECTED
} from "../../constants/ActionTypes";

//websocket-соенинеие с бэком установлено
export const onSocketConnected = () => { return { type: ON_SOCKS_CONNECTED } };

//websocket-соенинеие с бэком разорвано
export const onSocketDisconnected = () => { return { type: ON_SOCKS_DISCONNECTED } };

export const onMessageReceived = (msg: any) => { return { type: ON_MESSAGE_RECEIVED } }