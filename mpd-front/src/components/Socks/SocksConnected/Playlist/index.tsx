import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import {IPlaylistReducer} from "reducers/Playlist";
import {Table} from "react-bootstrap";
import Loading from "components/Loading";
import {IStatusReducer} from "reducers/Status";
import './index.css'
import {PlaylistPanel} from "./PlaylistPanel";
import {ICapturedObject} from "../../../../reducers/Dnd";

interface IPlaylistProps {
    playlist: IPlaylistReducer;
    playlistRequest: Function;
    status: IStatusReducer;
    playid: Function;
    captureObject: Function;
    capturedObject: ICapturedObject;
    addDir: Function;
    addFile: Function;
    releaseObject: Function;
    // items: IPlaylistItem[];
}

const mapStateToProps = (state: any) => {
    return {
        playlist: state.playlist,
        status: state.status,
        capturedObject: state.capturedObject
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        playlistRequest: Actions.playlistRequest,
        playid: Actions.playid,
        addDir: Actions.addToCurrentPlaylist,
        addFile: Actions.addFileToCurrentPlaylist,
        captureObject: Actions.captureObject,
        releaseObject: Actions.releaseObject
    }, dispatch);

const timeFormatter = (time: number) => {
    var date = new Date(0);
    date.setSeconds(time);
    let out = date.toISOString().substr(11, 8);
    return out;
};


const PlaylistComponent =
    (props: IPlaylistProps) => <Loading request={() => props.playlistRequest()} state={props.playlist}>
        <div className={props.capturedObject.type!=='none' ? 'full dragging' : 'full'}
             onMouseUp={e => props.releaseObject(props.capturedObject)}
        >
          <PlaylistPanel/>
          <Table>
            <thead>
            <tr>
              <th>track</th>
              <th>artist</th>
              <th>album</th>
              <th>title</th>
              <th>time</th>
            </tr>
            </thead>
            <tbody>
            {props.playlist.items.map(value => <tr key={value.id}
                                                   className={props.status.songid === value.id ? 'currentsong' : 'othersong'}
                                                   onDoubleClick={event1 => {props.playid(value.id)}}
                                                   onMouseUp={e => {e.stopPropagation(); props.releaseObject(props.capturedObject, value.pos);}}
            >
              <td>{value.track}</td>
              <td>{value.artist}</td>
              <td>{value.album}</td>
              <td>{value.title}</td>
              <td>{timeFormatter(value.time)}</td>
            </tr>)}
            </tbody>
          </Table>
        </div>
      </Loading>;
export const Playlist = connect(mapStateToProps, mapDispatchToProps)(PlaylistComponent);
