import { Middleware, MiddlewareAPI, Dispatch, Action } from "redux";

import * as actions from '../actions/index';

export const socketMiddleware = (function(){
    let socket : WebSocket;
    let connected : boolean = false;

    const onOpen = (ws : any,store : any, token: any) => (evt: any) => {
        //Send a handshake, or authenticate with remote end

        //Tell the store we're connected
        connected = true;
        store.dispatch(actions.onSocketConnected());
    };

    const onClose = (ws : any,store : any) => (evt: any) => {
        //Tell the store we've disconnected
        connected = false;
        store.dispatch(actions.onSocketDisconnected());
    };

    const onMessage = (ws : any,store : any) => (evt: any) => {
        //Parse the JSON message received on the websocket
        var msg = JSON.parse(evt.data);
        switch(msg.type) {
            case "CHAT_MESSAGE":
                //Dispatch an action that adds the received message to our state
                store.dispatch(actions.onMessageReceived(msg));
                break;
            default:
                console.log("Received unknown message type: '" + msg.type + "'");
                break;
        }
    };

    return (store : any) => (next : any) => (action : any) => {
        switch(action.type) {

            //The user wants us to connect
            case 'CONNECT':
                //Start a new connection to the server
                if(connected) {
                    socket.close();
                    connected = false;
                }
                //Send an action that shows a "connecting..." status for now
                store.dispatch(actions.onSocketConnected());

                //Attempt to connect (we could send a 'failed' action on error)
                socket = new WebSocket(action.url);
                socket.onmessage = onMessage(socket,store);
                socket.onclose = onClose(socket,store);
                socket.onopen = onOpen(socket,store,action.token);

                break;

            //The user wants us to disconnect
            case 'DISCONNECT':
                if(connected) {
                    socket.close();
                }

                //Set our state to disconnected
                store.dispatch(actions.onSocketDisconnected());
                break;

            //Send the 'SEND_MESSAGE' action down the websocket to the server
            case 'SEND_CHAT_MESSAGE':
                socket.send(JSON.stringify(action));
                break;

            //This action is irrelevant to us, pass it on to the next middleware
            default:
                return next(action);
        }
    }

})();

// export default socketMiddleware;

//
// export const loggerMiddleware: Middleware = (api: MiddlewareAPI) =>
//     (next: Dispatch) =>
//         <A extends Action>(action: A): A => {
//
//         switch (action.type) {
//             case 'CONNECT':
//                 break;
//             case 'DISCONNECT':
//                 break;
//             default:
//                 return next(action);
//
//         }
//             console.log("Before");
//             console.log(action);
//             const result = next(action);
//             console.log("After");
//             return result;
//         };
//
// const onOpen = (token: any) => (evt: any) => {
//     console.log('WS is onOpen');
//     console.log('token ' + token);
//     console.log('evt ' + evt.data);
// };
//
// const onClose = () => (evt: any) => {
//     console.log('WS is onClose');
//     console.log('evt ' + evt.data);
// };