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

interface IPlaylistProps {
    playlist: IPlaylistReducer;
    playlistRequest: Function;
    status: IStatusReducer;
    playid: Function;
    // items: IPlaylistItem[];
}

const mapStateToProps = (state: any) => {
    return {
        playlist: state.playlist,
        status: state.status
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        playlistRequest: Actions.playlistRequest,
        playid: Actions.playid
    }, dispatch);

const timeFormatter = (time: number) => {
    var date = new Date(0);
    date.setSeconds(time);
    let out = date.toISOString().substr(11, 8);
    return out;
};
const PlaylistComponent =
    (props: IPlaylistProps) => <Loading request={() => props.playlistRequest() } state={props.playlist}>
        <>
            <PlaylistPanel/>
        <Table>
        <thead>
        <tr>
            <th>track</th>
            <th>artist</th>
            <th>album</th>
            <th>title</th>
            {/*<th>file</th>*/}
            {/*<th>id</th>*/}
            {/*<th>pos</th>*/}
            <th>time</th>
        </tr>
        </thead>
        <tbody>
        {props.playlist.items.map(value => <tr key={value.id} className={props.status.songid === value.id ? 'currentsong' : 'othersong'}
        onDoubleClick={event1 => {
            props.playid(value.id)}}>
            <td>{value.track}</td>
            <td>{value.artist}</td>
            <td>{value.album}</td>
            <td>{value.title}</td>
            {/*<td>{value.file}</td>*/}
            {/*<td>{value.id}</td>*/}
            {/*<td>{value.pos}</td>*/}
            <td>{timeFormatter(value.time)}</td>
        </tr> )}
        </tbody>
    </Table>
        </>
</Loading>;

export const Playlist = connect(mapStateToProps, mapDispatchToProps)(PlaylistComponent);
