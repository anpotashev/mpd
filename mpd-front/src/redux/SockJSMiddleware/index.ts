import * as Actions from 'actions';
import {SEND_MESSAGE, SOCKS_CONNECT} from "constants/ActionTypes";
import {CompatClient, IMessage, Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {Destinations, WS_ROOT} from "constants/Socks";
import {topics} from "constants/topics";


export const socketMiddleware = (function () {
    let client: CompatClient;
    client = Stomp.over(function () {
        return new SockJS(WS_ROOT);
    });
    let connected: boolean = false;


    const onOpen = (store: any) => {
        connected = true;
        topics.forEach(value => client.subscribe(value, (msg) => onMessage(store, msg)));
        store.dispatch(Actions.onSocketConnected());
        store.dispatch(Actions.sendMessage(Destinations.CONNECTION_STATE, {}));
    };

    const onClose = (store: any) => {
        connected = false;
        store.dispatch(Actions.onSocketDisconnected());
    };

    const onMessage = (store: any, message: IMessage) => {
        store.dispatch(Actions.processMessage(message));
    };

    const tryConnect = (store: any) => {
        client.onWebSocketClose = () => {
            onClose(store);
            setTimeout(() => tryConnect(store), 1000);
        };
        client.onConnect = () => onOpen(store);
        client.onreceive = (message) => onMessage(store, message);
        client.debug = console.log;
        client.activate();
    };

    return (store: any) => (next: any) => (action: any) => {
        switch (action.type) {
            case SOCKS_CONNECT:
                if (connected) {
                    return;
                }
                tryConnect(store);
                break;
            case SEND_MESSAGE:
                client.send(action.payload.destination, {}, action.payload.msg);
                break;
            default:
                return next(action);
        }
    }

})();
