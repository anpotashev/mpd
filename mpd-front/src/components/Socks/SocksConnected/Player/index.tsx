import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import {IStatusReducer} from "reducers/Status";
import Loading from "../../../Loading";

interface IPlayerProps {
    playerRequest: Function;
    statusRequest: Function;
    status: IStatusReducer;
}

const mapStateToProps = (state: any) => {
    return {
        status: state.status
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        playerRequest: Actions.playerRequest,
        statusRequest: Actions.statusRequest
    }, dispatch);

const MyButton = (props: any) => <button type="button"
                                         className="btn btn-lg"
                                         aria-label="Left Align"
                                         onClick={props.playerCommand}
                                         disabled={props.disable}
>
    <span className={props.className}></span>
</button>;


const PlayerComponent = (props: IPlayerProps) => <Loading state={props.status} request={props.statusRequest}>
        <div className="playerbtn">

            <MyButton disable={props.status.state === "stop"}
                      playerCommand={() => props.playerRequest('PREV')}
                      className="glyphicon glyphicon-fast-backward"/>
            <MyButton disable={props.status.state === "play"}
                      playerCommand={() => props.playerRequest('PLAY')}
                      className="glyphicon glyphicon-play"/>
            <MyButton disable={props.status.state === "pause" || props.status.state === "stop"}
                      playerCommand={() => props.playerRequest('PAUSE')}
                      className="glyphicon glyphicon-pause"/>
            <MyButton disable={props.status.state === "stop"}
                      playerCommand={() => props.playerRequest('STOP')}
                      className="glyphicon glyphicon-stop"/>
            <MyButton disable={props.status.state === "stop"}
                      playerCommand={() => props.playerRequest('NEXT')}
                      className="glyphicon glyphicon-fast-forward"/>
        </div>
    </Loading>;


export const Player = connect(mapStateToProps, mapDispatchToProps)(PlayerComponent);
