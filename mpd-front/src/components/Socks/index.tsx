import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { SocksNotConnected } from "./SocksNotConnected";
import {SocksConnected} from "./SocksConnected";
import {IProps} from "./types";
import * as Actions from "../../actions/index";
import SocksClient from "./SocksClient/index";
import {createRef} from "react";

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

class Socks extends React.Component<IProps> {
    private client = createRef<SocksClient>();
    render() {
        return <>
            <SocksClient onConnect={this.onConnect} onDisconnect={
                this.props.onSocketDisonnected} subscriptions={[]}
                         ref={this.client}
            />
            {this.props.socksConnected
                ? this.printConnected()
                : this.printNotConnected()
            }
        </>;
    }

    printConnected = () => <SocksConnected/>;

    printNotConnected = () => <SocksNotConnected/>;

    onConnect = () => {
        this.props.onSocketConnected()
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Socks);