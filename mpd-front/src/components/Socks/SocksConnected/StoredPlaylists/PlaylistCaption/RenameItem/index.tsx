import * as React from 'react';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as Actions from "actions";

export interface IRenameProps {
    oldName: string;
    renameStoredPlaylist: Function;
}

interface IRenameState {
    newName: string;
}


const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        renameStoredPlaylist: Actions.renameStoredPlaylist
    }, dispatch);

class RenameItem extends React.Component<IRenameProps, IRenameState> {

    state: IRenameState = {
        newName: this.props.oldName
    };

    render() {
        return <>
            <input type="text" value={this.state.newName} size={10} onChange={(e) => {this.setState({newName: e.target.value})}}></input>
            <button type="button"
                     className="glyphicon glyphicon-floppy-save"
                     aria-label="Left Align"
                     onClick={() => this.props.renameStoredPlaylist(this.props.oldName, this.state.newName)}
                     disabled={this.state.newName.length == 0 || this.props.oldName == this.state.newName}
            ></button>
        </>;
    }

}
export default connect(null, mapDispatchToProps)(RenameItem);