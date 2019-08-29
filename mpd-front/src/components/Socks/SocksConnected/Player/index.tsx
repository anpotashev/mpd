import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import {Destinations} from "constants/Socks";
import {Button} from "react-bootstrap";

interface IConnectionMenuProps {
    sendMessage: Function;
}

const mapStateToProps = (state: any) => {
    return {
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        sendMessage: Actions.sendMessage
    }, dispatch);

const PlayerComponent = (props: IConnectionMenuProps) => <>
    <Button onClick={() => props.sendMessage(Destinations.PLAYER, "PREV" )}>PREV</Button>
    <Button onClick={() => props.sendMessage(Destinations.PLAYER, "PLAY" )}>PLAY</Button>
    <Button onClick={() => props.sendMessage(Destinations.PLAYER, "PAUSE" )}>PAUSE</Button>
    <Button onClick={() => props.sendMessage(Destinations.PLAYER, "STOP" )}>STOP</Button>
    <Button onClick={() => props.sendMessage(Destinations.PLAYER, "NEXT" )}>NEXT</Button>
</>;

export const Player = connect(mapStateToProps, mapDispatchToProps)(PlayerComponent);
