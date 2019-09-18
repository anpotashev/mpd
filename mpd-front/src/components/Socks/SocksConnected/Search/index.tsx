import * as React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Actions from "actions";
import {Button, Label, Modal} from "react-bootstrap";
import {Conditions, NamedSearchCondition} from "../../../../reducers/Search";
import EditComponentWindow from "./EditConditionWindow";

const mapStateToProps = (state: any) => {
    return {
        conditions: state.search
    }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        startEditing: Actions.startEdit,
        search: Actions.search,
    }, dispatch);

export interface ISearchProps {
    conditions: Conditions;
    startEditing: Function;
    search: Function;
}

class SearchComponent extends React.Component<ISearchProps, any> {
    state = {
        showWindow: false,
    };
    render() {
        console.log('1111', JSON.stringify(this.props.conditions, null, 2));
        return <>
            <Label>Search</Label>
            {this.props.conditions.conditions.map((value, key) => <div key={key}><Label>{value.name}</Label><Button onClick={() => {this.props.search(value.condition)}}>search</Button></div>)}
            <Button onClick={()=>this.props.startEditing()}>New</Button>
            <EditComponentWindow/>
        </>
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SearchComponent)