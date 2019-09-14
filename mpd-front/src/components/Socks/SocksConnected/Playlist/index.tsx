import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import {IPlaylistItem, IPlaylistReducer} from "reducers/Playlist";
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
    move: Function;
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
        releaseObject: Actions.releaseObject,
        move: Actions.moveInPlaylist
    }, dispatch);

const timeFormatter = (time: number) => {
    var date = new Date(0);
    date.setSeconds(time);
    let out = date.toISOString().substr(11, 8);
    return out;
};

function canProcessReleaseAction(value: IPlaylistItem, capturedObject: ICapturedObject) : boolean {
  return capturedObject.type === 'pos' && capturedObject.pos !== value.pos
}

function getClassName(props: IPlaylistProps, value: IPlaylistItem, capturedObject: ICapturedObject) {
  let result = props.status.songid === value.id ? 'currentsong' : 'othersong';
  if (canProcessReleaseAction(value, capturedObject)) {
    result += ' dragging';
  }
  return result
}

const PlaylistComponent =
    (props: IPlaylistProps) => <Loading request={() => props.playlistRequest()} state={props.playlist}>
        <div className={(props.capturedObject.type!=='none'&& props.capturedObject.type !=='pos') ? 'full dragging' : 'full'}
             onMouseUp={e => props.releaseObject(props.capturedObject)}
        >
          <PlaylistPanel/>
          <Table className="disable-select">
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
                                                   className={getClassName(props, value, props.capturedObject)}
                                                   onDoubleClick={event1 => {props.playid(value.id)}}
                                                   onMouseUp={e => {
                                                      e.stopPropagation();
                                                      if (canProcessReleaseAction(value, props.capturedObject)) {
                                                        props.releaseObject(props.capturedObject, value.pos);
                                                      } else {
                                                        props.captureObject({
                                                          type: 'none',
                                                          path: ''
                                                        });
                                                      }
                                                   }}
                                                   onMouseDown={e=>{e.stopPropagation();
                                                   props.captureObject({path:'', type: 'pos', pos: value.pos})}}
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
