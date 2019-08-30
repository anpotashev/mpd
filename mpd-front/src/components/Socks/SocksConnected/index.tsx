import * as React from 'react';
import {Menu} from "./menu";
import {connect} from "react-redux";
import {Player} from "./Player";

interface ISockConnectedProps {
    connected: boolean;
}

const mapStateToProps = (state: any) => {
    return {
        connected: state.mpdConnection
    }
};

export const SocksConnectedComponent = (props: ISockConnectedProps) => <>
    <Menu/>
    {props.connected ? <Player/> : <></> }

</>;


export const SocksConnected = connect(mapStateToProps)(SocksConnectedComponent);