import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import DummyConnection from "./dummy";
import {Destinations} from "constants/Socks";

interface IConnectionMenuProps {
    connected: boolean;
    sendMessage: Function;
}

const mapStateToProps = (state: any) => {
    return {
        connected: state.mpdConnection
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        sendMessage: Actions.sendMessage
    }, dispatch);

const Connection = (props: IConnectionMenuProps) => <DummyConnection
    checked={props.connected}
    click={() => props.sendMessage(props.connected ? Destinations.DISCONNECT : Destinations.CONNECT, {})}
/>;

export const ConnectionMenu = connect(mapStateToProps, mapDispatchToProps)(Connection);