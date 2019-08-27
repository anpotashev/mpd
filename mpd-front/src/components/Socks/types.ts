export interface IProps {
    socksConnected: boolean;
    onSocketConnected: Function;
    onSocketDisonnected: Function;
    socksConnect: Function;
    sendMessage: Function;
}

export interface ISubscription {
    topic: string;
    action: any;
}