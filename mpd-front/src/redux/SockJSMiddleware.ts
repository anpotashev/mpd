import * as actions from 'actions';
import {SEND_MESSAGE, SOCKS_CONNECT} from "constants/ActionTypes";
import {CompatClient, IMessage, Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {WS_ROOT} from "constants/Socks";
import {topics} from "constants/topics";


export const socketMiddleware = (function(){
    let client: CompatClient;
    client = Stomp.over(function () {
        return new SockJS(WS_ROOT);
    });
    let connected : boolean = false;


    const onOpen = (store : any) => {
        connected = true;
        topics.forEach(value => client.subscribe(value, (msg) => onMessage(store, msg)));
        store.dispatch(actions.onSocketConnected());
    };

    const onClose = (store : any) => {
        connected = false;
        store.dispatch(actions.onSocketDisconnected());
    };

    const onMessage = (store: any, message: IMessage) => {
        console.log("onMessage");
        console.log(JSON.stringify(message.body, null, 2));

    };

    const tryConnect = (store : any) => {
        client.onWebSocketClose = () => { onClose(store);
            setTimeout(() => tryConnect(store), 1000);
        };
        client.onConnect = () => onOpen(store);
        client.onreceive = (message) => onMessage(store, message);
        client.debug = (msg) => console.log(msg);
        client.activate();
    };

    return (store : any) => (next : any) => (action : any) => {
        switch(action.type) {
            case SOCKS_CONNECT:
                if(connected) {
                    return;
                }
                tryConnect(store);
                break;
            case SEND_MESSAGE:
                client.send(action.payload.destination, action.payload.msg);
                break;
            default:
                return next(action);
        }
    }

})();
