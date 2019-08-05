export interface IProps {
    socksConnected: boolean;
    onSocketConnected: Function;
    onSocketDisonnected: Function;
}

export interface ISubscription {
    topic: string;
    action: any;
}