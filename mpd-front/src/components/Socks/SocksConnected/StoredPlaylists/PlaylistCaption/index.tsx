import * as React from 'react';
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {connect} from "react-redux";
import RenameItem from './RenameItem';
export interface IPlaylistCaptionProps {
    title: string;
    addToCurrentPlaylist: Function;
    loadStoredPlaylist: Function;
    rmStoredPlaylist: Function;
    renameStoredPlaylist: Function;
}


const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        addToCurrentPlaylist: Actions.addStoredPlaylist,
        loadStoredPlaylist: Actions.loadStoredPlaylist,
        rmStoredPlaylist: Actions.rmStoredPlaylist,
        renameStoredPlaylist: Actions.renameStoredPlaylist
    }, dispatch);


const PlaylistCaptionComponent = (props: IPlaylistCaptionProps) => <span className="dropdown">
        <button className="dropdown-toggle"
           data-toggle="dropdown">{props.title}</button>
        <ul className="dropdown-menu">
          <li><button className="dropdown-item nowrap"
            onClick={e => {props.addToCurrentPlaylist(props.title, 0)}}>add to current playlist at first</button></li>
          <li><button className="dropdown-item nowrap"
            onClick={e => {props.addToCurrentPlaylist(props.title)}}>add to current playlist at last</button></li>
          <li><button className="dropdown-item nowrap"
            onClick={e => {props.loadStoredPlaylist(props.title)}}>set as currentPlaylist</button></li>
          <li><button className="dropdown-item nowrap"
            onClick={e => {props.rmStoredPlaylist(props.title)}}>remove</button></li>
            <li><RenameItem oldName={props.title}/></li>
        </ul>
      </span>;

export const PlaylistCaption = connect(null, mapDispatchToProps)(PlaylistCaptionComponent);
