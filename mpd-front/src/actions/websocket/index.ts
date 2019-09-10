import {DEFAULT_TIMEOUT, WS_REQUEST, WsDestination} from "constants/Socks";
import {ON_SOCKS_CONNECTED, ON_SOCKS_DISCONNECTED, SEND_MESSAGE, SOCKS_CONNECT} from "constants/ActionTypes";
import {ISendMessagePayload} from "redux/SockJSMiddleware";


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

export const sendMessage = (payload: ISendMessagePayload) => {
    return {
        type: SEND_MESSAGE,
        payload: {
            type: payload.type,
            msg: payload.msg
        }
    }
};

export const playlistRequest = (timeout: number = DEFAULT_TIMEOUT) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYLIST,
            timeout: timeout,
            msg: {}
        }
    }
};

export const playerRequest = (cmd: string) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYER,
            msg: { cmd: cmd }
        }
    }
};

export const statusRequest = (timeout: number = DEFAULT_TIMEOUT) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.STATUS,
            timeout: timeout,
            msg: {}
        }
    }
};

export const checkConnectionState = (timeout: number = DEFAULT_TIMEOUT) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.CONNECTION_STATE,
            timeout: timeout,
            msg: {}
        }
    }
};

export const changeConnectionState = (newState: boolean) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.CHANGE_CONNECTION_STATE,
            msg: newState
        }
    }
};


export const playid = (id: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYER_PLAYID,
            msg: id
        }
    }
};

export const getTree = (timeout: number = DEFAULT_TIMEOUT) => {
    return {
        type: WS_REQUEST,
        payload: {
            timeout: timeout,
            type: WsDestination.TREE,
            msg: {}
        }
    }
};

export const addToCurrentPlaylist = (path: String, pos?: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYLIST_ADD,
            msg: {
                path: path,
                pos: pos
            }
        }
    }
}