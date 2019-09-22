import * as React from 'react';
import {connect} from 'react-redux';
import * as Actions from 'actions';
import {bindActionCreators} from 'redux';

interface AddUrlProps {
    addToCurrentPlaylist: Function;
}

interface AddUrlState {
    url: string
}

const mapStateToProps = (state: any) => {
    return {
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        addToCurrentPlaylist: Actions.addFileToCurrentPlaylist
    }, dispatch);

class AddUrl extends React.Component<AddUrlProps, AddUrlState> {
    state = {
        url:''
    };

    handleEditChange(e: any) {
        this.setState({url: e.target.value});
    }
    render () {
        return <>
            <input type="text" placeholder="stream url" value={this.state.url} onChange={this.handleEditChange.bind(this)}/>
            <button type="button" className="btn btn-lg" aria-label="Left Align" disabled={this.state.url.length===0}
                                                                      onClick={() => this.props.addToCurrentPlaylist(this.state.url)}>
            <span className="glyphicon glyphicon-plus" aria-hidden="true"></span></button></>
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(AddUrl);

