import * as React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Actions from "actions";
import {Button, Label} from "react-bootstrap";
import {Conditions} from "reducers/Search";
import SearchConditionConstructor from "components/SearchConditionConstructor";
import '../Tree/TreeLevel/index.css';
import '../Playlist/index.css';

const mapStateToProps = (state: any) => {
    return {
        conditions: state.search
    }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        startEditing: Actions.startEdit,
        search: Actions.search,
        searchAdd: Actions.addSearch,
        captureObject: Actions.captureObject
    }, dispatch);

export interface ISearchProps {
    conditions: Conditions;
    startEditing: Function;
    search: Function;
    searchAdd: Function;
    captureObject: Function;
}

class SearchComponent extends React.Component<ISearchProps, any> {
    state = {
        showWindow: false,
    };
    render() {
        return <>
            <Label>Search</Label>

            {this.props.conditions.conditions.map((value, key) => <li className="tree-li-directory disable-select" key={key}
                onMouseDown={()=>{
                    this.props.captureObject({path: value.name,
                        searchCondition: value.condition,
                        type: "search"})}}
            ><span className={'glyphicon glyphicon-folder-close span-li'}/> {value.name}
                {/*<Button onClick={() => {this.props.search(value.condition)}}>search</Button>*/}
                {/*<Button onClick={() => this.props.searchAdd(value.condition)}>add to playlist</Button>*/}
            </li>)}
            <Button onClick={()=>this.props.startEditing()}>New</Button>
            <SearchConditionConstructor/>
        </>
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SearchComponent)