import * as React from 'react';
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {connect} from "react-redux";
import 'components/Socks/SocksConnected/Tree/TreeLevel/index.css';
import {NamedSearchCondition} from "reducers/Search";

export interface ISearchCaptionProps {
    condition: NamedSearchCondition;
    addSearch: Function;
    removeSearch: Function;
    editSearch: Function;
}

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        addSearch: Actions.addSearch,
        removeSearch: Actions.removeSearchCondition,
        editSearch: Actions.startEdit
    }, dispatch);

const SearchCaptionComponent = (props: ISearchCaptionProps) => <span className="dropdown">
    <button className="dropdown-toggle"
            data-toggle="dropdown">{props.condition.name}</button>
    <ul className="dropdown-menu">
        <li><button className="dropdown-item nowrap"
                    onClick={e => props.addSearch(props.condition.condition, 0)}>add to current playlist at first</button></li>
        <li><button className="dropdown-item nowrap"
                    onClick={e => props.addSearch(props.condition.condition)}>add to current playlist at last</button></li>
        <li><button className="dropdown-item nowrap"
                    onClick={e => props.editSearch(props.condition.name)}><span className={'glyphicon glyphicon-edit'}/> Edit</button></li>
        <li><button className="dropdown-item nowrap"
                    onClick={e => {}}>Rename</button></li>
        <li><button className="dropdown-item nowrap"
                    onClick={e => props.removeSearch(props.condition.name)}><span className={'glyphicon glyphicon-remove'}/> Delete</button></li>
    </ul>
</span>;

export const SearchCaption = connect(null, mapDispatchToProps)(SearchCaptionComponent);