import * as React from 'react';
import {IErrorReducer} from "reducers/Errors";
import {connect} from "react-redux";
import {Button, Panel} from "react-bootstrap";
import {bindActionCreators} from "redux";
import * as Actions from "../../actions";

interface IErrorsPanelProps {
    errors: IErrorReducer;
    clearErrors: Function
}

const mapStateToProps = (state: any) => {
    return {
        errors: state.errors
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        clearErrors: Actions.clearErrors
    }, dispatch);

const ErrorsPanelComponent = (props : IErrorsPanelProps) => (props.errors.errors.length > 0 )
    ? <Panel bsStyle="danger" eventKey="1"
             className="sticky-panel">
        <Panel.Heading>
            <Panel.Title componentClass="h3" toggle>There are some errors. <Button onClick={()=>props.clearErrors()}>Clear</Button>
            </Panel.Title>
        </Panel.Heading>
        <Panel.Body collapsible>
            {props.errors.errors.map(
                (value, key : any) => <div className="alert alert-danger" role="alert"
                                                 key={key}>{value.type}: {value.message}</div>)}
        </Panel.Body>
    </Panel>
    : <></>;

export const ErrorsPanel = connect(mapStateToProps, mapDispatchToProps)(ErrorsPanelComponent);