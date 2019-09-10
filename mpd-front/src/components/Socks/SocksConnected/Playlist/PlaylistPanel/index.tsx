import * as React from 'react';
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {connect} from "react-redux";

interface IPlaylistPanelProps {
    clearPlaylist: Function;
    shufflePlaylist: Function;
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        clearPlaylist: Actions.clearPlaylist,
        shufflePlaylist: Actions.shufflePlaylist
    }, dispatch);


const PlaylistPanelComponent = (props: IPlaylistPanelProps) => <div text-align="center">
  <button type="button" className="btn btn-lg" aria-label="Left Align"
          onClick={() => props.clearPlaylist()}>
    <span className="glyphicon glyphicon-trash" aria-hidden="true"></span>
  </button>
    <button type="button" className="btn btn-lg" aria-label="Left Align"
            onClick={() => props.shufflePlaylist()}>
        <span className="glyphicon glyphicon-random" aria-hidden="true"></span>
    </button>
</div>;


export const PlaylistPanel = connect(null, mapDispatchToProps)(PlaylistPanelComponent);