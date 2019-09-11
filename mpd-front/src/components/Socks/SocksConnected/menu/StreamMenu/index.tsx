import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import {MenuItem, NavDropdown} from "react-bootstrap";
import {CheckedUncheckedElement} from "components/CheckedUncheckedElement";

interface IStreamMenuProps {
    streamEnable: boolean;
    changeStreamingState: Function;
}

const mapStateToProps = (state: any) => {
    return {
        streamEnable: state.stream.enableStreaming
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        changeStreamingState: Actions.changeStreaming
    }, dispatch);

const StreamingMenuComponent = (props: IStreamMenuProps) => <NavDropdown title="Stream player" id="basic-nav-dropdown">
    <MenuItem onClick={() => props.changeStreamingState(!props.streamEnable)}><CheckedUncheckedElement
        checked={props.streamEnable}>Streaming</CheckedUncheckedElement></MenuItem>
</NavDropdown>;

export const StreamingMenu = connect(mapStateToProps, mapDispatchToProps)(StreamingMenuComponent);