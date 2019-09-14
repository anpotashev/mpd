import {ILoading} from 'reducers';
import {FAILED_SUFFIX, LOADING, PROCESSING_SUFFIX, SUCCESS_SUFFIX} from "redux/SockJSMiddleware2";
import {CAPTURE_PATH, CONNECTION_STATE, OUTPUT} from "constants/ActionTypes";

export type CapturedObjectType = 'file' | 'directory' | 'playlist' | 'pos' | 'none'

export interface ICapturedObject {
  path: string;
  type: CapturedObjectType;
  pos?: number;
}

const defaultValue : ICapturedObject = {
  path: '',
  type: "none"
};


export default function (state: ICapturedObject = defaultValue, action: any): ICapturedObject {
  switch (action.type) {
    case CAPTURE_PATH:
      return action.payload;
    default:
      return state;
  }
}