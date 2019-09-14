import * as React from 'react';
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {connect} from "react-redux";
import {Player} from "../../Player";
import {ICapturedObject} from "reducers/Dnd";
import '../index.css';

interface IPlaylistPanelProps {
    clearPlaylist: Function;
    shufflePlaylist: Function;
    capturedObject: ICapturedObject;
    releaseObject: Function;
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
      releaseObject: Actions.releaseObject
    }, dispatch);


const PlaylistPanelComponent = (props: IPlaylistPanelProps) => <div text-align="center">
  <button type="button" className={props.capturedObject.type === 'pos' ? "btn btn-lg dragging" : "btn btn-lg"} aria-label="Left Align"
          onClick={() => props.clearPlaylist()}
          onMouseUp={e => {e.stopPropagation();
            if(props.capturedObject.type==='pos') {props.releaseObject(props.capturedObject, -1);}
          }}
  >
    <span className="glyphicon glyphicon-trash" aria-hidden="true"></span>
  </button>
    <button type="button" className="btn btn-lg" aria-label="Left Align"
            onClick={() => props.shufflePlaylist()}>
        <span className="glyphicon glyphicon-random" aria-hidden="true"></span>
    </button>
    <Player/>
</div>;


export const PlaylistPanel = connect(mapStateToProps, mapDispatchToProps)(PlaylistPanelComponent);