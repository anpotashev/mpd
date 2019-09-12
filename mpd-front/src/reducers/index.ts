import {combineReducers} from "redux";
import SocksConnection from "./SocksConnection";
import MpdConnection from "./MpdConnection";
import Status from "./Status";
import Playlist from "./Playlist";
import Tree from "./Tree";
import Stream from "./Stream";
import Outputs from "./Outputs";
import ShortStatus from "./ShortStatus";
import StoredPlaylists from "./StoredPlaylists";
import {LOADING} from "../redux/SockJSMiddleware2";

const reducers = combineReducers({
    socksConnection: SocksConnection,
    mpdConnection: MpdConnection,
    playlist: Playlist,
    status: Status,
    tree: Tree,
    stream: Stream,
    outputs: Outputs,
    storedPlaylists: StoredPlaylists,
    shortStatus: ShortStatus
});
export default reducers;

export interface ILoading {
    requestStatus: LOADING
}