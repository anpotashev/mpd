import * as React from 'react';
import './index.css';
import {ILoading} from "reducers";
import {LOADING} from "redux/SockJSMiddleware2";

const LoadingComponent = () => <><span className="glyphicon glyphicon-refresh glyphicon-refresh-animate"/> Loading...</>;

export interface ILoadingProps {
    request: Function;
    state: ILoading;
    children: any;
}

const Loading = (props: ILoadingProps) => {
    switch (props.state.requestStatus) {
        case LOADING.notLoading:
            props.request();
            return <></>;
        case LOADING.loading:
            return <LoadingComponent/>;
        default: return props.children;
    }
};
export default Loading;