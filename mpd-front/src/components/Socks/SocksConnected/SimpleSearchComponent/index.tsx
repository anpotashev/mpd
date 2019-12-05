import * as React from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as Actions from "actions";

const mapStateToProps = (state: any) => {
    return {
    }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        search: Actions.searchNew
    }, dispatch);

interface ISimpleSearchProps {
    search: any;
}

interface ISimpleSearchState {
    text: string;
}

class SimpleSearch extends React.Component<ISimpleSearchProps, ISimpleSearchState>{
    state: ISimpleSearchState = {
        text: ""
    };
    render() {
        return <><input value={this.state.text} onChange={e => this.handleChange(e)}/>
            <button onClick={e=>this.props.search(this.state.text)}>search</button></>;
    }

    private handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        this.setState({text: e.target.value});
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SimpleSearch)