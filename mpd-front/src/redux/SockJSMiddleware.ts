import * as actions from '../actions/index';
import {SEND_MESSAGE, SOCKS_CONNECT} from "../constants/ActionTypes";
import {CompatClient, IMessage, Stomp} from "@stomp/stompjs";
import {WS_ROOT} from "../constants/Socks";
import SockJS from "sockjs-client";


export const socketMiddleware = (function(){
    let client: CompatClient;
    client = Stomp.over(function () {
        return new SockJS(WS_ROOT);
    });
    let connected : boolean = false;


    const onOpen = (store : any) => {
        console.log("connected socks");
        connected = true;
        client.subscribe("/user/queue/reply", (msg) => onMessage(store, msg));
        client.subscribe("/topic/connection", (msg) => onMessage(store, msg));
        client.subscribe("/topic/connection1", (msg) => onMessage(store, msg));
        store.dispatch(actions.onSocketConnected());
    };

    const onClose = (store : any) => {
        console.log("disconnected socks");
        connected = false;
        store.dispatch(actions.onSocketDisconnected());
    };

    const onMessage = (store: any, message: IMessage) => {
        console.log("onMessage");
        console.log(JSON.stringify(message.body, null, 2));

    };

    return (store : any) => (next : any) => (action : any) => {
        switch(action.type) {
            case SOCKS_CONNECT:
                if(connected) {
                    return;
                }
                client.onWebSocketClose = () => onClose(store);
                client.onConnect = () => onOpen(store);
                client.onreceive = (message) => onMessage(store, message);
                client.debug = (msg) => console.log(msg);
                client.activate();
                break;
            case SEND_MESSAGE:
                client.send(action.payload.destination, action.payload.msg);
                break;
            default:
                return next(action);
        }
    }

})();
