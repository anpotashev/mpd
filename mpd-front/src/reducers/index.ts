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
import CapturedObject from "./Dnd";
import Search from "./Search";
import Errors from "./Errors";
import {LOADING} from "../redux/SockJSMiddleware2";
import SearchResult from "./SearchResult";

const reducers = combineReducers({
    socksConnection: SocksConnection,
    mpdConnection: MpdConnection,
    playlist: Playlist,
    status: Status,
    tree: Tree,
    stream: Stream,
    outputs: Outputs,
    storedPlaylists: StoredPlaylists,
    shortStatus: ShortStatus,
    capturedObject: CapturedObject,
    search: Search,
    errors: Errors,
    searchResult: SearchResult
});
export default reducers;

export interface ILoading {
    requestStatus: LOADING
}