import {ISubscription} from "../types";


export interface IStompClientProps {
    onConnect: any;
    onDisconnect: any;
    subscriptions: ISubscription[];
}
