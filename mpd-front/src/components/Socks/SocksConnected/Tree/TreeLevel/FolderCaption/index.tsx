import * as React from 'react';
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {connect} from "react-redux";
import '../index.css';

export interface IFolderCaptionProps {
    path: string;
    title: string;
    addToPlaylist: Function;
    updateDb: Function
}

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        addToPlaylist: Actions.addToCurrentPlaylist,
        updateDb: Actions.updateDb
    }, dispatch);

const FolderCaptionComponent = (props: IFolderCaptionProps) => <span className="dropdown">
    <button className="dropdown-toggle"
        data-toggle="dropdown">{props.title}</button>
    <ul className="dropdown-menu">
        <li><button className="dropdown-item"
               onClick={e => props.addToPlaylist(props.path, 0)}>add to current playlist at first</button></li>
        <li><button className="dropdown-item"
               onClick={e => props.addToPlaylist(props.path)}>add to current playlist at last</button></li>
        <li><button className="dropdown-item"
            onClick={e => props.updateDb('')}>update music database (full)</button></li>
        <li><button className="dropdown-item"
            onClick={e => props.updateDb(props.path)}>update music database (from here)</button></li>
    </ul>
</span>;

export const FolderCaption = connect(null, mapDispatchToProps)(FolderCaptionComponent);