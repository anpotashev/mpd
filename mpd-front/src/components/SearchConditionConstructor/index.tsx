import * as React from 'react';
import {Button, Modal, Form, FormControl, Row, Col} from "react-bootstrap";
import {IBaseSearchCondition, PredicateCondition, PredicateType, SearchCondition} from "../../reducers/Search";

interface ISearchConditionConstructorState {
    condition?: IBaseSearchCondition;
    selectedElement?: any;
    parentElement: PredicateCondition | undefined;
}

export default class SearchConditionConstructor extends React.Component<any, ISearchConditionConstructorState> {

    state = {
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
        parentElement: undefined
    };
    render(): React.ReactElement<any, string | React.JSXElementConstructor<any>> | string | number | {} | React.ReactNodeArray | React.ReactPortal | boolean | null | undefined {
        return <>
            <Modal.Dialog>
                <Modal.Header>
                    <Modal.Title>Edit condition</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Button disabled={this.state.selectedElement === undefined}>NOT</Button>
                    <Button disabled={this.state.selectedElement === undefined} onClick={()=>this.addOr()}>OR</Button>
                    <Button disabled={this.state.selectedElement === undefined}>AND</Button>
                    <Form>
                        <Row>
                            <Col>
                                <FormControl componentClass="select">
                                    <option>ARTIST</option>
                                    <option>ALBUM</option>
                                    <option>TITLE</option>
                                </FormControl>
                            </Col>
                            <Col>
                                <FormControl componentClass="select">
                                    <option>START_WITH</option>
                                    <option>CONTAINS</option>
                                    <option>REGEX</option>
                                </FormControl>
                                <Col>
                                <FormControl componentClass="input"/>
                                </Col>
                                <Col><Button disabled={this.state.selectedElement === undefined}>add</Button></Col>
                            </Col>
                        </Row>
                    </Form>
                    <ul>
                        {this.printCondition(this.state.condition)}
                    </ul>
                    <hr></hr>
                    {JSON.stringify(this.state, null, 2)}
                </Modal.Body>
            </Modal.Dialog>
        </>;
    }

    printLi(element: any, caption: string, parentElement?: PredicateCondition) {
        if (element == this.state.selectedElement) {
            return <div onClick={() => this.setState(Object.assign({}, this.state, {selectedElement: undefined, parentElement: undefined}))}><b>{caption}</b></div>
        }
        return <div onClick={() => this.setState(Object.assign({}, this.state, {selectedElement: element, parentElement: parentElement}))}>{caption}</div>
    }

    printCondition(condition?: any, parentElement?: PredicateCondition) {
        if (condition === undefined || condition === null) return <li>{this.printLi(condition, 'null', undefined)}</li>;
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

    private addOr() {
        console.log('here', JSON.stringify(this.state, null, 2));
        if (this.state.parentElement === undefined) {
            console.log('-------------', JSON.stringify(Object.assign({}, this.state, {selectedElement: undefined, condition: {type: "OR", conditions: [null, null]}})))
            this.setState(Object.assign({}, this.state, {selectedElement: undefined, condition: {type: "OR", conditions: [null, null]}}));
        } else {
            let never: PredicateCondition = this.state.parentElement!;
            let index = never.conditions.findIndex(value => value===this.state.selectedElement);
            console.log(index);
            never.conditions[index] = {type: "OR", conditions: [null, null]};
            this.setState(Object.assign({}, this.state, {parentElement: undefined, selectedElement: undefined}));
        }
        // let predicateCondition: PredicateCondition = {
        //     type: "OR",
        //     conditions: [null, null]
        // };
        // this.setState({selectedElement: predicateCondition});

    }
}
/*
type Id3Tag = 'ARTIST' | 'ALBUM' | 'TITLE';
type Operation = 'START_WITH' | 'CONTAINS' | 'REGEX';
type PredicateType = 'AND' | 'OR' | 'NOT';
 */