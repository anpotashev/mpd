import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import {MenuItem, NavDropdown} from "react-bootstrap";
import {CheckedUncheckedElement} from "components/CheckedUncheckedElement";
import {IStatusReducer} from "../../../../../reducers/Status";
import {LOADING} from "../../../../../redux/SockJSMiddleware2";

interface IStreamMenuProps {
    status: IStatusReducer;
    random: Function;
    single: Function;
    consume: Function;
    repeat: Function;
}

const mapStateToProps = (state: any) => {
    return {
        status: state.status
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        random: Actions.setRandom,
        single: Actions.setSingle,
        consume: Actions.setConsume,
        repeat: Actions.setRepeat
    }, dispatch);

const OptionsMenuComponent = (props: IStreamMenuProps) => {
    return props.status.requestStatus === LOADING.loaded ? <NavDropdown title="Options" id="basic-nav-dropdown">
            <MenuItem onClick={() => props.random(!props.status.random)}><CheckedUncheckedElement
                checked={props.status.random}>Random</CheckedUncheckedElement></MenuItem>
            <MenuItem onClick={() => props.single(!props.status.single)}><CheckedUncheckedElement
                checked={props.status.single}>Single</CheckedUncheckedElement></MenuItem>
            <MenuItem onClick={() => props.consume(!props.status.consume)}><CheckedUncheckedElement
                checked={props.status.consume}>Consume</CheckedUncheckedElement></MenuItem>
            <MenuItem onClick={() => props.repeat(!props.status.repeat)}><CheckedUncheckedElement
                checked={props.status.repeat}>Repeat</CheckedUncheckedElement></MenuItem>
        </NavDropdown>
        : <></>
    ;
}

export const OptionsMenu = connect(mapStateToProps, mapDispatchToProps)(OptionsMenuComponent);