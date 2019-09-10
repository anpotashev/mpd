import {combineReducers} from "redux";
import SocksConnection from "./SocksConnection";
import MpdConnection from "./MpdConnection";
import Status from "./Status";
import Playlist from "./Playlist";
import Tree from "./Tree";
import {LOADING} from "../redux/SockJSMiddleware2";

const reducers = combineReducers({
    socksConnection: SocksConnection,
    mpdConnection: MpdConnection,
    playlist: Playlist,
    status: Status,
    tree: Tree
});
export default reducers;

export interface ILoading {
    requestStatus: LOADING
}