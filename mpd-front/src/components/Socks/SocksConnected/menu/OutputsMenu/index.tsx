import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';
import {MenuItem, NavDropdown} from "react-bootstrap";
import {CheckedUncheckedElement} from "components/CheckedUncheckedElement";
import {LOADING} from "redux/SockJSMiddleware2";
import {IOutputsReducer} from "reducers/Outputs";

interface IStreamMenuProps {
    outputs: IOutputsReducer;
    saveOutput: Function;
    getOutputs: Function;
}

const mapStateToProps = (state: any) => {
    return {
        outputs: state.outputs
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        saveOutput: Actions.saveOutput,
        getOutputs: Actions.getOutputs
    }, dispatch);

const OutputsMenuComponent = (props: IStreamMenuProps) => {
    switch (props.outputs.requestStatus) {
        case LOADING.notLoading:
            props.getOutputs();
            return <></>;
        case LOADING.loading:
            return <></>;
        default:
            // return <></>;
            return <NavDropdown title="Outputs" id="basic-nav-dropdown">
            {props.outputs.outputs.map(value => <MenuItem key={value.id}
                onClick={() => props.saveOutput(Object.assign({},value, {enabled: !value.enabled}))}><CheckedUncheckedElement
            checked={value.enabled}>{value.name}</CheckedUncheckedElement></MenuItem>)}
        </NavDropdown>
    }
};

export const OutputsMenu = connect(mapStateToProps, mapDispatchToProps)(OutputsMenuComponent);