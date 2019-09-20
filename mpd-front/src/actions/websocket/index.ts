import {DEFAULT_TIMEOUT, WS_REQUEST, WsDestination} from "constants/Socks";
import {ON_SOCKS_CONNECTED, ON_SOCKS_DISCONNECTED, SEND_MESSAGE, SOCKS_CONNECT} from "constants/ActionTypes";
import {ISendMessagePayload} from "redux/SockJSMiddleware";
import {IOutput} from "../../reducers/Outputs";
import {IBaseSearchCondition} from "../../reducers/Search";


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

//Отправить сообщение. Не использовать вне middleware.
export const sendMessage = (payload: ISendMessagePayload) => {
    return {
        type: SEND_MESSAGE,
        payload: {
            type: payload.type,
            msg: payload.msg
        }
    }
};

//Запрос текущего плейлиста
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

//Очистить текущий плейлист
export const clearPlaylist = () => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.CLEAR_PLAYLIST,
            msg: {}
        }
    }
};

//Перемешать текущий плейлист
export const shufflePlaylist = () => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.SHUFFLE_PLAYLIST,
            msg: {}
        }
    }
};

//Отправить команду плейеру
export const playerRequest = (cmd: string) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYER,
            msg: { cmd: cmd }
        }
    }
};

export const playerSeek = (songPos: number, seekPos: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYER_SEEK,
            msg: { songPos: songPos, seekPos: seekPos }
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
};
export const addFileToCurrentPlaylist = (path: String, pos?: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYLIST_ADD_FILE,
            msg: {
                path: path,
                pos: pos
            }
        }
    }
};

export const updateDb = (path: String) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.UPDATE_DB,
            msg: {
                path: path
            }
        }
    }
};

export const getStreamUrl = (timeout: number = DEFAULT_TIMEOUT) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.GET_STREAM_URL,
            timeout: timeout,
            msg: {}
        }
    }
};

export const setConsume = (newState: boolean) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.SET_CONSUME,
            msg: newState
        }
    }
};
export const setRandom = (newState: boolean) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.SET_RANDOM,
            msg: newState
        }
    }
};
export const setRepeat = (newState: boolean) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.SET_REPEAT,
            msg: newState
        }
    }
};
export const setSingle = (newState: boolean) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.SET_SINGLE,
            msg: newState

        }
    }
};

export const getOutputs = (timeout: number = DEFAULT_TIMEOUT) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.GET_OUTPUTS,
            timeout: timeout,
            msg: {}
        }
    }
};

export const saveOutput = (output: IOutput) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.SET_OUTPUT,
            msg: output
        }
    }
};

export const getStoredPlaylists = (timeout: number = DEFAULT_TIMEOUT) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.STORED_PLAYLISTS,
            timeout: timeout,
            msg: {}
        }
    }
};
export const loadStoredPlaylist = (playlist: string) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.STORED_PLAYLISTS_LOAD,
            msg: {storedPlaylist: playlist}
        }
    }
};
export const addStoredPlaylist = (playlist: string, pos?: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.STORED_PLAYLISTS_ADD,
            msg: {storedPlaylist: playlist, pos: pos}
        }
    }
};
export const saveStoredPlaylist = (name: string) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.STORED_PLAYLISTS_SAVE,
            msg: {name: name}
        }
    }
};
export const rmStoredPlaylist = (name: string) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.STORED_PLAYLISTS_RM,
            msg: {name: name}
        }
    }
};
export const renameStoredPlaylist = (oldName: string, newName: string) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.STORED_PLAYLISTS_RENAME,
            msg: {oldName: oldName, newName: newName}
        }
    }
};

export const deleteFromPlaylist = (pos: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.DELETE_FROM_PLAYLIST,
            msg: {pos: pos}
        }
    }
};

export const moveInPlaylist = (from: number, to: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.MOVE_IN_PLAYLIST,
            msg: {from: from, to: to}
        }
    }
};

export const search = (condition: IBaseSearchCondition) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.SEARCH,
            msg: condition
        }
    }
};

export const addSearch = (condition: IBaseSearchCondition, pos?: number) => {
    return {
        type: WS_REQUEST,
        payload: {
            type: WsDestination.PLAYLIST_ADD_SEARCH,
            msg: {condition: condition, pos: pos}
        }
    }
};
