import {combineReducers} from "redux";
import SocksConnection from "./SocksConnection";
import MpdConnection from "./MpdConnection";

const reducers = combineReducers({
    socksConnection: SocksConnection,
    mpdConnection: MpdConnection
});
export default reducers;