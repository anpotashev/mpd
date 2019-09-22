import * as React from 'react';
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {connect} from "react-redux";
import {Player} from "../../Player";
import {ICapturedObject} from "reducers/Dnd";
import '../index.css';
import AddUrl from "./AddUrl";

interface IPlaylistPanelProps {
    clearPlaylist: Function;
    shufflePlaylist: Function;
    capturedObject: ICapturedObject;
    releaseObject: Function;
    captureObject: Function;
};

const mapStateToProps = (state: any) => {
  return {
    capturedObject: state.capturedObject
  }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        clearPlaylist: Actions.clearPlaylist,
        shufflePlaylist: Actions.shufflePlaylist,
        releaseObject: Actions.releaseObject,
        captureObject: Actions.captureObject
    }, dispatch);


const PlaylistPanelComponent = (props: IPlaylistPanelProps) => <div text-align="center">
  <button type="button" className={props.capturedObject.type === 'pos' || props.capturedObject.type === 'search' ? "btn btn-lg dragging" : "btn btn-lg"} aria-label="Left Align"
          onClick={() => props.clearPlaylist()}
          onMouseUp={e => {e.stopPropagation();
            if(props.capturedObject.type==='pos'|| props.capturedObject.type === 'search') {props.releaseObject(props.capturedObject, -1);}
              props.captureObject({
                type: 'none',
                path: ''
              });

          }}
  >
    <span className="glyphicon glyphicon-trash" aria-hidden="true"></span>
  </button>
    <button type="button" className="btn btn-lg" aria-label="Left Align"
            onClick={() => props.shufflePlaylist()}>
        <span className="glyphicon glyphicon-random" aria-hidden="true"></span>
    </button>
    <AddUrl/>
    <Player/>
</div>;


export const PlaylistPanel = connect(mapStateToProps, mapDispatchToProps)(PlaylistPanelComponent);