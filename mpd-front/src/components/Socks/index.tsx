import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { SocksNotConnected } from "./SocksNotConnected";
import {SocksConnected} from "./SocksConnected";
import {IProps} from "./types";
import * as Actions from "../../actions/index";
import SocksClient from "./SocksClient/index";
import {createRef} from "react";
import {Button} from "react-bootstrap";

const mapStateToProps = (state: any) => {
    return {
        socksConnected: state.socksConnection.connected
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        onSocketConnected: Actions.onSocketConnected,
        onSocketDisonnected: Actions.onSocketDisconnected,
        socksConnect: Actions.socksConnect,
        sendMessage: Actions.sendMessage
    }, dispatch);

class Socks extends React.Component<IProps> {

    componentDidMount(): void {
        console.log('hhh');
        this.props.socksConnect();
    }

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
            <Button onClick={() => this.props.socksConnect()}>connect</Button>

            <Button onClick={() => this.props.sendMessage('/mpd/connectionState', {})} >state</Button>
            <Button onClick={() => this.props.sendMessage('/mpd/disconnect', {})} >disconnect</Button>
            <Button onClick={() => this.props.sendMessage('/mpd/connect', {})} >connect</Button>
        </>;
    }

    printConnected = () => <SocksConnected/>;

    printNotConnected = () => <SocksNotConnected/>;

    onConnect = () => {
        this.props.onSocketConnected()
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Socks);