import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import DummyConnection from "./dummy";
import {IMpdConnection} from "reducers/MpdConnection";

interface IConnectionMenuProps {
    connectionState: IMpdConnection;
    changeConnectionState: Function;
}

const mapStateToProps = (state: any) => {
    return {
        connectionState: state.mpdConnection
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        changeConnectionState: Actions.changeConnectionState
    }, dispatch);

const Connection = (props: IConnectionMenuProps) => {
    return <DummyConnection
        checked={props.connectionState.connected}
        // click={() => props.connect(!props.connectionState.connected, 1000)}
        click={() => props.changeConnectionState(!props.connectionState.connected)}
    />;
};
export const ConnectionMenu = connect(mapStateToProps, mapDispatchToProps)(Connection);