import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { SocksNotConnected } from "./SocksNotConnected";
import {SocksConnected} from "./SocksConnected";
import {IProps} from "./types";
import * as Actions from "../../actions/index";
import SocksClient from "./SocksClient/index";

const mapStateToProps = (state: any) => {
    return {
        socksConnected: state.socksConnection.connected
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        onSocketConnected: Actions.onSocketConnected,
        onSocketDisonnected: Actions.onSocketDisconnected
    }, dispatch);

const SocksComponent = (props: IProps) => <>
        <SocksClient onConnect={props.onSocketConnected} onDisconnect={props.onSocketDisonnected} subscriptions={[]}/>
        {props.socksConnected
        ? printConnected()
        : printNotConnected()
    }
    </>;

const printConnected = () => <SocksConnected/>;

const printNotConnected = () => <SocksNotConnected/>;

export const Socks = connect(mapStateToProps, mapDispatchToProps)(SocksComponent);