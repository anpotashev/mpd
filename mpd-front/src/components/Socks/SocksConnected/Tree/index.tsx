import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import { bindActionCreators } from 'redux';
import Loading from "components/Loading";
import TreeLevel from "./TreeLevel";
import {ITreeReducer} from "reducers/Tree";

export interface ITreeProps {
    getTree: Function;
    tree: ITreeReducer;
}

const mapStateToProps = (state: any)  => {
  return {
    tree: state.tree
  }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        getTree: Actions.getTree
    }, dispatch);

const TreeComponent = (props: ITreeProps) => <Loading request={props.getTree} state={props.tree}>
            <TreeLevel open={true} element={props.tree.root} path=""/>
    </Loading>;

export const Tree = connect(mapStateToProps, matDispatchToProps)(TreeComponent);
