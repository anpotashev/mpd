import {CAPTURE_PATH} from "constants/ActionTypes";
import {IBaseSearchCondition} from "../Search";

export type CapturedObjectType = 'file' | 'directory' | 'playlist' | 'pos' | 'search' | 'none'

export interface ICapturedObject {
  path: string;
  type: CapturedObjectType;
  pos?: number;
  searchCondition?: IBaseSearchCondition;
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