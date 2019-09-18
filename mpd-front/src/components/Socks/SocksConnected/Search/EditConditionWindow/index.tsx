import * as React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as Actions from "actions";
import {Button, Label, Modal} from "react-bootstrap";
import {NamedSearchCondition} from "reducers/Search";

const mapStateToProps = (state: any) => {
    return {
        show: state.search.isEditing,
    }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        cancelEdit: Actions.cancelEdit,
        saveChanges: Actions.saveSearchCondition,
    }, dispatch);

export interface EditComponentWindowProps {
    show: boolean;
    cancelEdit: Function;
    saveChanges: Function;
}

class EditComponentWindow extends React.Component<EditComponentWindowProps, {text:string}> {
    state = {
        text: ''
    };
    render() {

        return this.props.show ? <Modal.Dialog>
            <Modal.Header>
                <Modal.Title>Edit condition</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <textarea
                    name="asdf"
                    cols={50}
                    rows={10}
                    value={this.state.text}
                    onChange={e => {
                        console.log('change', e);
                        this.setState({text: e.target.value})
                    }}
                ></textarea>
            </Modal.Body>

            <Modal.Footer>
                <Button  onClick={()=>this.props.cancelEdit()}>Close</Button>
                <Button  onClick={()=>this.props.saveChanges("AAAAA",JSON.parse(this.state.text))}>Save changes</Button>
            </Modal.Footer>
        </Modal.Dialog>
            : <></>;
    }
}

export default connect(mapStateToProps, matDispatchToProps)(EditComponentWindow)