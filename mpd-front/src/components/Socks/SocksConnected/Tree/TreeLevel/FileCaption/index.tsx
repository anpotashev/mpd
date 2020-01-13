import * as React from 'react';
import * as Actions from "actions";
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import '../index.css';

export interface IFileCaptionProps {
    path: string;
    title: string;
    hint?: string;
    addToPlaylist: Function;
    catpureOject: Function;
}


const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        addToPlaylist: Actions.addFileToCurrentPlaylist,
        catpureOject: Actions.captureObject
    }, dispatch);


const FileCaptionComponent = (props: IFileCaptionProps) => <li className="tree-li-file" title={props.hint}
                                                               onMouseDown={e => { e.stopPropagation(); props.catpureOject({path:props.path, type:'file'})}}
><span
    className="glyphicon glyphicon-file"/>
  <span className="dropdown">
        <button className="dropdown-toggle"
           data-toggle="dropdown">{props.title}</button>
        <ul className="dropdown-menu">
            <li><button className="dropdown-item nowrap"
                onClick={e => props.addToPlaylist(props.path, 0)}>add to current playlist at first</button></li>
            <li><button className="dropdown-item nowrap"
                onClick={e => props.addToPlaylist(props.path)}>add to current playlist at last</button></li>
        </ul>
      </span>
</li>;

export const FileCaption = connect(null, mapDispatchToProps)(FileCaptionComponent)

