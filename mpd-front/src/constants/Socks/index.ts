// declare const IS_PRODUCTION: boolean;

// export const WS_ROOT = IS_PRODUCTION ? 'websocket' : '/ws';
export const WS_ROOT = '/stomp';

export const CONNECT = '/topic/connect';
export const PLAYLIST = '/topic/playlist';
export const STATUS = '/topic/status';
export const PLAY_TIME = '/topic/songTime';
export const OUTPUTS = '/topic/output';

export enum Destinations {
    CONNECTION_STATE = "/mpd/connectionState"
    , CONNECT = "/mpd/connect"
    , DISCONNECT = "/mpd/disconnect"
    , PLAYER = "/mpd/player"
}