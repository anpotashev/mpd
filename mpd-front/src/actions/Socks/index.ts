import {
    ON_MESSAGE_RECEIVED,
    ON_SOCKS_CONNECTED,
    ON_SOCKS_DISCONNECTED, SEND_MESSAGE, SOCKS_CONNECT
} from "../../constants/ActionTypes";

//websocket-соенинеие с бэком установлено
export const onSocketConnected = () => {
    console.log("onSocketConnected");
    return { type: ON_SOCKS_CONNECTED } };

//websocket-соенинеие с бэком разорвано
export const onSocketDisconnected = () => {
    console.log("onSocketDisconnected");
        return { type: ON_SOCKS_DISCONNECTED } };

export const onMessageReceived = (msg: any) => { return { type: ON_MESSAGE_RECEIVED } };

export const socksConnect = (msg: any) => {
    console.log("socksConnect");
    return { type: SOCKS_CONNECT } };

export const sendMessage = (destination: String, msg: any) => {
    console.log("sendMessage");
    return { type: SEND_MESSAGE,
            payload: {
                destination: destination,
                msg: msg
            }};
};