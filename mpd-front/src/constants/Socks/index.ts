// declare const IS_PRODUCTION: boolean;

// export const WS_ROOT = IS_PRODUCTION ? 'websocket' : '/ws';
export const WS_ROOT = '/stomp';

export const DEFAULT_TIMEOUT = 1000;

export const WS_SUCCESS = 'WS_SUCCESS';
export const WS_FAILED = 'WS_FAILED';
export const WS_CHECK = 'WS_CHECK';
export const WS_REQUEST = 'WS_REQUEST';

export class WsDestination {
    static CONNECTION_STATE = new WsDestination("/mpd/connectionState", "CONNECTION_STATE");
    static PLAYER = new WsDestination("/mpd/player", "PLAYER");
    static PLAYLIST = new WsDestination("/mpd/playlist", "PLAYLIST");
    static PLAYLIST_ADD = new WsDestination("/mpd/playlist/add", "PLAYLIST");
    static CLEAR_PLAYLIST = new WsDestination("/mpd/playlist/clear", "CLEAR_PLAYLIST");
    static CHANGE_CONNECTION_STATE = new WsDestination("/mpd/connectionState/change", "CHANGE_CONNECTION_STATE");
    static STATUS = new WsDestination("/mpd/status", "STATUS");
    static PLAYER_PLAYID = new WsDestination("/mpd/player/playid", "PLAYER_PLAYID");
    static TREE = new WsDestination("/mpd/tree", "TREE");

    private constructor(private destination: string, private type: string) {}

    getDestination() {
        return this.destination;
    }

    getType() {
        return this.type;
    }
}