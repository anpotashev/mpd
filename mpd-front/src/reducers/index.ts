import {combineReducers} from "redux";
import SocksConnection from "./SocksConnection";

const reducers = combineReducers({
    socksConnection: SocksConnection
});
export default reducers;