import {ON_SOCKS_CONNECTED, ON_SOCKS_DISCONNECTED, SEND_MESSAGE, SOCKS_CONNECT} from "../../constants/ActionTypes";
import {IMessage} from "@stomp/stompjs";
import {Destinations} from "../../constants/Socks";

//websocket-соенинеие с бэком установлено
export const onSocketConnected = () => {
    return {type: ON_SOCKS_CONNECTED};
};

//websocket-соенинеие с бэком разорвано
export const onSocketDisconnected = () => {
    return {type: ON_SOCKS_DISCONNECTED};
};

//Установить веб-сокетное соединение
export const socksConnect = () => {
    return {type: SOCKS_CONNECT};
};

export const sendMessage = (destination: Destinations, msg: any) => {
    return {
        type: SEND_MESSAGE,
        payload: {
            destination: destination,
            msg: msg
        }
    };
};

export const processMessage = (message: IMessage) => {
    let body = JSON.parse(message.body);
    return {
        type: body.type,
        payload: body.payload
    };
};
