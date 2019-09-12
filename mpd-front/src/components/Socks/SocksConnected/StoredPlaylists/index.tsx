import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import Loading from "components/Loading";
import {IStoredPlaylistReducer} from "reducers/StoredPlaylists";
import StoredPlaylist from "./StoredPlaylist";
import SaveForm from './SaveForm';

interface IStoredProps {
    getStored: Function;
    stored: IStoredPlaylistReducer;
}

const mapStateToProps = (state: any) => {
    return {
        stored: state.storedPlaylists
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        getStored: Actions.getStoredPlaylists
    }, dispatch);

const StoredPlaylistsComponent = (props: IStoredProps) => <Loading state={props.stored} request={props.getStored}>
    <ul className="tree-ul">
        {props.stored.playlists.map((value, key) => <StoredPlaylist title={value.name} items={value.playlistItems} key={key}/>)}
        <li  className="tree-li-directory">
        <SaveForm/>
        </li>
    </ul>
</Loading>;


export const StoredPlaylists = connect(mapStateToProps, mapDispatchToProps)(StoredPlaylistsComponent);
