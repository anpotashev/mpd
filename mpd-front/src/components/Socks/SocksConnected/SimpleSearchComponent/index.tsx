import * as React from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import SearchResult from "./SearchResults";
import {ISearchResult} from "../../../../reducers/SearchResult";

const mapStateToProps = (state: any) => {
    return {
        searchResult: state.searchResult
    }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        search: Actions.searchNew,
        clearSearch: Actions.clearSearch,
    }, dispatch);

interface ISimpleSearchProps {
    searchResult: ISearchResult;
    search: any;
    clearSearch: any;
}

interface ISimpleSearchState {
    text: string;
    checked: string[];
}

class SimpleSearch extends React.Component<ISimpleSearchProps, ISimpleSearchState>{
    state: ISimpleSearchState = {
        text: "",
        checked: [
            // "file",
            "path",
            "title",
            "artist",
            "album",
            "albumArtist",
            "genre"
        ]
    };
    render() {
        return <>
            <input type="checkbox" onChange={event => this.handleChangeCheckbox("file")} checked={this.state.checked.indexOf("file") > -1}/><label>file</label><br/>
            <input type="checkbox" onChange={event => this.handleChangeCheckbox("path")} checked={this.state.checked.indexOf("path") > -1}/><label>path</label><br/>
            <input type="checkbox" onChange={event => this.handleChangeCheckbox("title")} checked={this.state.checked.indexOf("title") > -1}/><label>title</label><br/>
            <input type="checkbox" onChange={event => this.handleChangeCheckbox("artist")} checked={this.state.checked.indexOf("artist") > -1}/><label>artist</label><br/>
            <input type="checkbox" onChange={event => this.handleChangeCheckbox("album")} checked={this.state.checked.indexOf("album") > -1}/><label>album</label><br/>
            <input type="checkbox" onChange={event => this.handleChangeCheckbox("albumArtist")} checked={this.state.checked.indexOf("albumArtist") > -1}/><label>albumArtist</label><br/>
            <input type="checkbox" onChange={event => this.handleChangeCheckbox("genre")} checked={this.state.checked.indexOf("genre") > -1}/><label>genre</label><br/>
            <input value={this.state.text} onChange={e => this.handleChange(e)}/>
            <br/>
            <SearchResult/>
            {this.props.searchResult.hasMore && <button onClick={event => this.search(true)}>show all ({this.props.searchResult.totalCount})</button>}
            </>;
    }

    private handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        let newState = this.state;
        newState.text = e.target.value;
        this.setState(newState);
        this.search();
    }

    private handleChangeCheckbox(label: string) {
        let newState = this.state;
        let indexOf = newState.checked.indexOf(label);
        if (indexOf > -1) {
            newState.checked.splice(indexOf, 1);
        } else {
            if (label === 'file') {
                this.remove(newState, 'path');
            }
            if (label === 'path') {
                this.remove(newState, 'file');
            }
            newState.checked.push(label);
        }
        this.setState(newState);
        this.search()
    }

    private remove(newState: ISimpleSearchState, label: string) {
        let indexOf = newState.checked.indexOf(label);
        if (indexOf >-1) {
            newState.checked.splice(indexOf, 1);
        }
    }



    private search(showAll: boolean = false) {
        if (this.state.text !== '' && this.state.checked.length > 0) {
            if (showAll) {
                this.props.search(this.state.text, this.state.checked, 0, this.props.searchResult.totalCount);
            } else {
                this.props.search(this.state.text, this.state.checked);
            }
        } else {
            this.props.clearSearch();
        }
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SimpleSearch)