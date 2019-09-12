import * as React from 'react';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as Actions from "actions";

interface ISaveFormProps {
    save: Function
}

interface ISaveFormState {
    text: string;
}


const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        save: Actions.saveStoredPlaylist
    }, dispatch);

class SaveForm extends React.Component<ISaveFormProps, ISaveFormState> {

    state: ISaveFormState = {
        text: ""
    };

    render() {
        return <>
            <input type="text" size={10} onChange={(e) => {this.setState({text: e.target.value})}}/>
            <button type="button"
                     className="glyphicon glyphicon-floppy-save"
                     aria-label="Left Align"
                     onClick={() => this.props.save(this.state.text)}
                     disabled={this.state.text.length === 0}
            />
        </>;
    }

}
export default connect(null, mapDispatchToProps)(SaveForm);