import * as React from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {ISearchResult} from "reducers/SearchResult";
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
                {this.props.searchResult.items.map((value, index) => <FileCaption key={index} path={value.path} title={value.title || value.file}/>)}
                </>;
        }
        return <>
            no result found...
        </>;
    }

}

export default connect(mapStateToProps, matDispatchToProps)(SearchResult)
