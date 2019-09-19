import * as React from 'react';
import {Button, Modal, Form, FormControl, Row, Col} from "react-bootstrap";
import {
    IBaseSearchCondition,
    Id3Tag,
    Operation,
    PredicateCondition,
    PredicateType,
    SearchCondition
} from "../../reducers/Search";
import {bindActionCreators} from "redux";
import * as Actions from "../../actions";
import {connect} from "react-redux";

interface SearchConditionConstructorProps {
    cancelEdit: Function;
    saveChanges: Function;
    show: boolean;
}
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


interface ISearchConditionConstructorState {
    condition?: IBaseSearchCondition;
    selectedElement?: any;
    parentElement: PredicateCondition | undefined;
    searchCondition: SearchCondition;
    name: string
}

interface NullCondition extends IBaseSearchCondition {
    asdf: any
}

class SearchConditionConstructor extends React.Component<SearchConditionConstructorProps, ISearchConditionConstructorState> {

    state = {
        searchCondition: {
            id3Tag: "ARTIST" as Id3Tag,
            operation: 'CONTAINS' as Operation,
            value: ''
        },
        selectedElement: undefined,
        condition: JSON.parse("{\n" +
            "\"type\": \"NOT\",\n" +
            " \"conditions\": [\n" +
            " {\n" +
            "  \"conditions\": [\n" +
            "    {\n" +
            "      \"id3Tag\": \"ALBUM\",\n" +
            "      \"operation\": \"CONTAINS\",\n" +
            "      \"value\": \"asdf\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id3Tag\": \"ALBUM\",\n" +
            "      \"operation\": \"CONTAINS\",\n" +
            "      \"value\": \"asdf\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id3Tag\": \"ALBUM\",\n" +
            "      \"operation\": \"START_WITH\",\n" +
            "      \"value\": \"afdsafdsa\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"type\": \"OR\"\n" +
            "}]\n" +
            " }"),
        parentElement: undefined,
        name: '12345'
    };

    handleId3TagSelectChanged(event:any){
        let sc = Object.assign({},this.state.searchCondition, {id3Tag: event.target.value});
        this.setState(Object.assign({}, this.state, {searchCondition: sc}));

    }
    handleOperationSelectChanged(event:any){
        let sc = Object.assign({},this.state.searchCondition, {operation: event.target.value});
        this.setState(Object.assign({}, this.state, {searchCondition: sc}));
    }
    handleValue(event:any){
        let sc = Object.assign({},this.state.searchCondition, {value: event.target.value});
        this.setState(Object.assign({}, this.state, {searchCondition: sc}));
    }
    handleName(event:any){
        this.setState(Object.assign({}, this.state, {name: event.target.value}));
    }

    render(): React.ReactElement<any, string | React.JSXElementConstructor<any>> | string | number | {} | React.ReactNodeArray | React.ReactPortal | boolean | null | undefined {
        if (!this.props.show) return <></>;
        return <>
            <Modal.Dialog>
                <Modal.Header>
                    <Modal.Title>Edit condition</Modal.Title>

                </Modal.Header>
                <Modal.Body>
                    <FormControl componentClass="input" onChange={this.handleName.bind(this)} value={this.state.name}/>
                    <Button disabled={this.state.selectedElement === undefined} onClick={()=>this.addCondition('NOT')}>NOT</Button>
                    <Button disabled={this.state.selectedElement === undefined} onClick={()=>this.addCondition('OR')}>OR</Button>
                    <Button disabled={this.state.selectedElement === undefined }onClick={()=>this.addCondition('AND')}>AND</Button>
                    <Form>
                        <Row>
                            <Col>
                                <FormControl
                                    componentClass="select" onChange={this.handleId3TagSelectChanged.bind(this)}>
                                    <option>ARTIST</option>
                                    <option>ALBUM</option>
                                    <option>TITLE</option>
                                </FormControl>
                            </Col>
                            <Col>
                                <FormControl componentClass="select" onChange={this.handleOperationSelectChanged.bind(this)}>
                                    <option>START_WITH</option>
                                    <option>CONTAINS</option>
                                    <option>REGEX</option>
                                </FormControl>
                                <Col>
                                <FormControl componentClass="input" onChange={this.handleValue.bind(this)}/>
                                </Col>
                                <Col><Button disabled={this.state.selectedElement === undefined} onClick={() => {this.addSearchCondition()}}>add</Button></Col>
                            </Col>
                        </Row>
                    </Form>
                    <ul>
                        {this.printCondition(this.state.condition)}
                    </ul>
                    <hr></hr>
                    <Button disabled={this.state.name.length===0 || !this.isValid(this.state.condition)} onClick={()=>this.props.saveChanges(this.state.name, this.state.condition)}>Save</Button>
                </Modal.Body>
            </Modal.Dialog>
        </>;
    }

    private addSearchCondition() {
        if (this.state.parentElement === undefined) {
            this.setState(Object.assign({}, this.state, {condition: this.state.searchCondition}));
        } else {
            let parentElement: PredicateCondition = this.state.parentElement!;
            let index = parentElement.conditions.findIndex(value => value===this.state.selectedElement);
            parentElement.conditions[index] = this.state.searchCondition;
            this.setState(Object.assign({}, this.state, {parentElement: undefined, selectedElement: undefined}));
        }
    }

    printLi(element: any, caption: string, parentElement?: PredicateCondition) {
        if (element == this.state.selectedElement) {
            return <div onClick={() => this.setState(Object.assign({}, this.state, {selectedElement: undefined, parentElement: undefined}))}><b>{caption}</b></div>
        }
        return <div onClick={() => this.setState(Object.assign({}, this.state, {selectedElement: element, parentElement: parentElement}))}>{caption}</div>
    }

    printCondition(condition?: any, parentElement?: PredicateCondition) {
        if (condition === undefined) return <li>{this.printLi(condition, 'null', undefined)}</li>;
        if (condition.asdf !== undefined) return <li>{this.printLi(condition, 'null', parentElement)}</li>;
        if (condition.type) {
            let condition1 = condition as PredicateCondition;
             return <li>{this.printLi(condition1, condition1.type, parentElement)}
                 <ul>
                     { condition1.conditions.map((value:any) => this.printCondition(value, condition1))}
                 </ul>
             </li>
        }
        return <li>{this.printLi(condition,condition.id3Tag +" " + condition.operation + " " + condition.value, parentElement)}</li>;
    }

    private addCondition(type: PredicateType) {

        if (this.state.parentElement === undefined) {
            let newCondition: PredicateCondition;
            if (type !== 'NOT') {
                newCondition = {type: type, conditions: [{asdf: 'a'}, {asdf: 'a'}]};
            } else {
                newCondition = {type: 'NOT', conditions: [{asdf: 'a'}]};
            }
            this.setState(Object.assign({}, this.state, {
                selectedElement: undefined,
                condition: newCondition
            }));
        } else {
            let newCondition: PredicateCondition;
            if (type !== 'NOT') {
                newCondition = {type: type, conditions: [{asdf: 'a'}, {asdf: 'a'}]};
            } else {
                newCondition = {type: 'NOT', conditions: [{asdf: 'a'}]};
            }
            let parentElement: PredicateCondition = this.state.parentElement!;
            let index = parentElement.conditions.findIndex(value => value===this.state.selectedElement);
            parentElement.conditions[index] = newCondition;
            this.setState(Object.assign({}, this.state, {parentElement: undefined, selectedElement: undefined}));
        }
    }

    private isValid(condition: any) :boolean {
        console.log('checking...', JSON.stringify(condition, null, 2));
        if (condition.asdf !== undefined) {
            return false;
        }
        if (condition.id3Tag !== undefined) {
            return true;
        }
        let conditions : IBaseSearchCondition[] = condition.conditions;
        let result = true;
        conditions.map(value => {
            result = result && this.isValid(value);
        });
        return result;
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SearchConditionConstructor);