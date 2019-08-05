import * as React from 'react';
import {CompatClient, Stomp} from '@stomp/stompjs';
import SockJS from "sockjs-client";
import {WS_ROOT} from '../../../constants/Socks';
import {IStompClientProps} from "./types";
import {ISubscription} from "../types";


interface IStompClientState {
    connected: boolean;
    subscriptions: ISubscription[];
}

export default class SocksClient extends React.Component<IStompClientProps, IStompClientState> {

    client: CompatClient;

    constructor(props: IStompClientProps) {
        super(props);
        this.state = {
            connected: false,
            subscriptions: []
        };
        let ws : WebSocket = new SockJS(WS_ROOT);
        this.client = Stomp.over(ws);
    }

    componentDidMount(): void {
        this.connectAndReconnect();
    }

    private connectAndReconnect(): void {
        this.client.onWebSocketClose = () => {
            this.onDisconnect();
            setTimeout(() => {
                this.connectAndReconnect();
            }, 5000);
        };
        this.client.onConnect = () => this.onConnect();
        this.client.debug = () => { };
        this.client.activate();
    }

    private onConnect() {
        this.props.subscriptions.map(this.subscribe);
        this.props.onConnect();
    }

    private subscribe = (value: ISubscription) => {
        this.client.subscribe(value.topic, (msg: any) => value.action(JSON.parse(msg.body)));
    };

    private onDisconnect() {
        this.props.onDisconnect();
    }

    render(): React.ReactNode {
        return <></>;
    }
}