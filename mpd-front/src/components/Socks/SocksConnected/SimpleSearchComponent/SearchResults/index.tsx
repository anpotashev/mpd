import * as React from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {IItem, ISearchResult} from "reducers/SearchResult";
import {FileCaption} from "../../Tree/TreeLevel/FileCaption";

const mapStateToProps = (state: any) => {
    return {
        searchResult: state.searchResult
    }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        search: Actions.searchNew
    }, dispatch);

interface ISearchResultProps {
    searchResult: ISearchResult;
    search: any;
}

class SearchResult extends React.Component<ISearchResultProps, {}> {
    render(): React.ReactElement<any, string | React.JSXElementConstructor<any>> | string | number | {} | React.ReactNodeArray | React.ReactPortal | boolean | null | undefined {
        if (this.props.searchResult.items.length > 0) {
            return <>
                {this.props.searchResult.items.map((value, index) => <FileCaption key={index} path={value.path} title={value.title || value.file}
                                                                                  hint={this.hint(value)}/>)}
                </>;
        }
        return <>
            no result found...
        </>;
    }

    private hint(value: IItem) : string {
        let result: string = "file: " + value.file;
        if (value.album && value.album != null) { result =  result + "\x0A" + "album: " + value.album; }
        if (value.title && value.title != null) { result =  result + "\x0A" + "title: " + value.title; }
        if (value.artist && value.artist != null) { result =  result + "\x0A" + "artist: " + value.artist; }
        if (value.albumArtist && value.albumArtist != null) { result =  result + "\x0A" + "albumArtist: " + value.albumArtist; }
        if (value.title && value.title != null) { result =  result + "\x0A" + "genre: " + value.genre; }
        return result;
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SearchResult)
