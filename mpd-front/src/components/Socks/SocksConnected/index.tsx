import * as React from 'react';
import {Menu} from "./menu";
import {connect} from "react-redux";
import {Player} from "./Player";
import {IMpdConnection} from "reducers/MpdConnection";
import Loading from "../../Loading";
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {Bottom} from "./Bottom";

interface ISockConnectedProps {
    connectionState: IMpdConnection;
    checkConnectionState: Function;
}

const mapStateToProps = (state: any) => {
    return {
        connectionState: state.mpdConnection
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        checkConnectionState: Actions.checkConnectionState
    }, dispatch);

export const SocksConnectedComponent = (props: ISockConnectedProps) => <Loading request={props.checkConnectionState} state={props.connectionState}><>
                <Menu/>
                {props.connectionState.connected ? <>
                    <Player/>
                    <Bottom/></> : <></> }
    </></Loading>;


export const SocksConnected = connect(mapStateToProps, mapDispatchToProps)(SocksConnectedComponent);