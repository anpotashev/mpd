import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import { bindActionCreators } from 'redux';
import Loading from "../../../Loading";
import TreeLevel from "./TreeLevel";
import {ITreeReducer} from "../../../../reducers/Tree";

export interface ITreeProps {
    getTree: Function;
    tree: ITreeReducer;
    addDirToPlaylist: Function;
    // addDirToPlaylistFirst: any;
    // updateDb: any;
    // addFileToPlaylistFirst: any;
    // addFileToPlaylist: any;
    // tree: any;
}

const mapStateToProps = (state: any)  => {
  return {
    tree: state.tree
  }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        getTree: Actions.getTree,
        addDirToPlaylist: Actions.addToCurrentPlaylist
        // addDirToPlaylist: Actions.addDirToPlaylist,
        // addDirToPlaylistFirst: Actions.addDirToPlaylistFirst,
        // updateDb: Actions.updateDb,
        // addFileToPlaylistFirst: Actions.addDirToPlaylistFirst,
        // addFileToPlaylist: Actions.addFileToPlaylist
    }, dispatch);

const TreeComponent = (props: ITreeProps) => <Loading request={props.getTree} state={props.tree}>
        <>
            <TreeLevel open={true} element={props.tree.root} path="" addDirToPlaylist={props.addDirToPlaylist}/>
        </>
    </Loading>;

    {/*<DummyTree loading={props.tree.loading}*/}
    {/*tree={props.tree.tree}*/}
    {/*addDirToPlaylist={props.addDirToPlaylist}*/}
    {/*addDirToPlaylistFirst={props.addDirToPlaylistFirst}*/}
    {/*updateDb={props.updateDb}*/}
    {/*addFileToPlaylist={props.addFileToPlaylist}*/}
    {/*addFileToPlaylistFirst={props.addFileToPlaylistFirst}*/}
{/*/>;*/}

export const Tree = connect(mapStateToProps, matDispatchToProps)(TreeComponent);
