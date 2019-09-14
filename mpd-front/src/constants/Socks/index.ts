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
    static PLAYER_SEEK = new WsDestination("/mpd/player/seek", "PLAYER_SEEK");
    static PLAYLIST = new WsDestination("/mpd/playlist", "PLAYLIST");
    static PLAYLIST_ADD = new WsDestination("/mpd/playlist/add", "PLAYLIST_ADD");
    static PLAYLIST_ADD_FILE = new WsDestination("/mpd/playlist/addFile", "PLAYLIST_ADD_FILE");
    static DELETE_FROM_PLAYLIST = new WsDestination("/mpd/playlist/remove", "DELETE_FROM_PLAYLIST");
    static MOVE_IN_PLAYLIST = new WsDestination("/mpd/playlist/move", "DELETE_FROM_PLAYLIST");
    static CLEAR_PLAYLIST = new WsDestination("/mpd/playlist/clear", "CLEAR_PLAYLIST");
    static SHUFFLE_PLAYLIST = new WsDestination("/mpd/playlist/shuffle", "TREE");
    static CHANGE_CONNECTION_STATE = new WsDestination("/mpd/connectionState/change", "CHANGE_CONNECTION_STATE");
    static STATUS = new WsDestination("/mpd/status", "STATUS");
    static PLAYER_PLAYID = new WsDestination("/mpd/player/playid", "PLAYER_PLAYID");
    static TREE = new WsDestination("/mpd/tree", "TREE");
    static UPDATE_DB = new WsDestination("/mpd/updateDb", "UPDATE_DB");
    static GET_STREAM_URL = new WsDestination("/mpd/streamPlayer", "GET_STREAM_URL");

    static SET_REPEAT = new WsDestination("/mpd/status/repeat", "SET_REPEAT");
    static SET_CONSUME = new WsDestination("/mpd/status/consume", "SET_CONSUME");
    static SET_RANDOM = new WsDestination("/mpd/status/random", "SET_RANDOM");
    static SET_SINGLE = new WsDestination("/mpd/status/single", "SET_SINGLE");

    static GET_OUTPUTS = new WsDestination("/mpd/outputs", "GET_OUTPUTS");
    static SET_OUTPUT = new WsDestination("/mpd/output/change", "SET_OUTPUT");
    static STORED_PLAYLISTS = new WsDestination("/mpd/storedPlaylist", "STORED_PLAYLISTS");
    static STORED_PLAYLISTS_LOAD = new WsDestination("/mpd/storedPlaylist/load", "STORED_PLAYLISTS_LOAD");
    static STORED_PLAYLISTS_ADD = new WsDestination("/mpd/storedPlaylist/add", "STORED_PLAYLISTS_ADD");
    static STORED_PLAYLISTS_RM = new WsDestination("/mpd/storedPlaylist/rm", "STORED_PLAYLISTS_ADD");
    static STORED_PLAYLISTS_SAVE = new WsDestination("/mpd/storedPlaylist/save", "STORED_PLAYLISTS_ADD");
    static STORED_PLAYLISTS_RENAME = new WsDestination("/mpd/storedPlaylist/rename", "STORED_PLAYLISTS_ADD");

    private constructor(private destination: string, private type: string) {}

    getDestination() {
        return this.destination;
    }

    getType() {
        return this.type;
    }
}