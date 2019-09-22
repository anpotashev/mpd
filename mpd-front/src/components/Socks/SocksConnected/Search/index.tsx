import * as React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Actions from "actions";
import {Conditions} from "reducers/Search";
import SearchConditionConstructor from "components/SearchConditionConstructor";
import '../Tree/TreeLevel/index.css';
import '../Playlist/index.css';
import {SearchCaption} from "./SearchCaption";

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
    render() {
        return <>
            {this.props.conditions.conditions.map((value, key) => <li className="tree-li-directory disable-select" key={key}
                onMouseDown={()=>{
                    this.props.captureObject({path: value.name,
                        searchCondition: value.condition,
                        type: "search"})}}
            ><span className={'glyphicon glyphicon-search span-li'}/><SearchCaption condition={value}/>

                {/*<Button onClick={() => {this.props.search(value.condition)}}>search</Button>*/}
                {/*<Button onClick={() => this.props.searchAdd(value.condition)}>add to playlist</Button>*/}
            </li>)}
            <li className="tree-li-directory disable-select">
            <span className={'glyphicon glyphicon-plus span-li'} onClick={()=>this.props.startEditing()}><button className="nowrap">create</button></span>
            </li>
            {this.props.conditions.isEditing ? <SearchConditionConstructor/> :<></>}
        </>
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SearchComponent)