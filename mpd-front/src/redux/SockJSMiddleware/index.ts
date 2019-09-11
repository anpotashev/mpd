import {Store} from 'redux';
import * as Actions from 'actions';
import {SEND_MESSAGE, SOCKS_CONNECT} from "constants/ActionTypes";
import {CompatClient, IMessage, Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {WS_FAILED, WS_SUCCESS, WS_ROOT, WsDestination} from "constants/Socks";

export interface ISendMessagePayload {
    type: WsDestination;
    msg: any;
}

export interface IReceivedMessageBody {
    type: string;
    payload: any;
}

const topics: string[] = [
    "/topic/connection"
    , "/topic/playlist"
    , "/topic/status"
    , "/topic/fullTree"
    , "/topic/tree"
    , "/topic/songTime"
    , "/topic/output"
    , "/topic/storedPlaylists"
];

export const socketMiddleware = (function () {
    let client: CompatClient;
    client = Stomp.over(function () {
        return new SockJS(WS_ROOT);
    });

    let connected: boolean = false;

    const onOpen = (store: Store) => {
        connected = true;
        topics.forEach(value => client.subscribe(value, (msg) => processReply(store, msg)));
        client.subscribe("/user/queue/reply", (msg) => processReply(store, msg));
        client.subscribe("/user/queue/error", (msg) => processError(store, msg));
        store.dispatch(Actions.onSocketConnected());
    };

    const processReply = (store: Store, msg: IMessage) => {
        let body: IReceivedMessageBody = JSON.parse(msg.body);
        store.dispatch({type: WS_SUCCESS,
            payload: body
        });
    };

    const processError = (store: Store, msg: IMessage) => {
        let body = JSON.parse(msg.body);
        console.warn(body.type + ": ", body.msg);
        store.dispatch({type: WS_FAILED,
            payload: body
        });
    };

    const onClose = (store: Store) => {
        connected = false;
        store.dispatch(Actions.onSocketDisconnected());
    };

    const tryConnect = (store: Store) => {
        client.onWebSocketClose = () => {
            onClose(store);
            setTimeout(() => tryConnect(store), 5000);
        };
        client.onConnect = () => onOpen(store);
        // client.debug = console.log;
        client.debug = ()=>{};
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
                let payload: ISendMessagePayload = action.payload;
                if (!connected) {
                    store.dispatch({type: WS_FAILED, payload: {type: payload.type.getType(), msg: "websocket not connected"}});
                    break;
                }
                client.send(payload.type.getDestination(), {}, JSON.stringify(payload.msg));
                break;

            default:
                return next(action);
        }
    }

})();
