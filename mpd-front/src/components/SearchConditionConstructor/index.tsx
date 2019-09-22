import * as React from 'react';
import {
    Button,
    Modal,
    FormControl,
    FormGroup, ButtonGroup, Label
} from "react-bootstrap";
import {
    IBaseSearchCondition,
    Id3Tag, NamedSearchCondition,
    Operation,
    PredicateCondition,
    PredicateType,
    SearchCondition
} from "../../reducers/Search";
import {bindActionCreators} from "redux";
import * as Actions from "../../actions";
import {connect} from "react-redux";
import './index.css';

interface SearchConditionConstructorProps {
    cancelEdit: Function;
    saveChanges: Function;
    show: boolean;
    editCondition?: NamedSearchCondition;
}

const mapStateToProps = (state: any) => {
    return {
        show: state.search.isEditing,
        editCondition: state.search.editCondition
    }
};
const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        cancelEdit: Actions.cancelEdit,
        saveChanges: Actions.saveSearchCondition,
    }, dispatch);


interface ISearchConditionConstructorState {
    condition?: IBaseSearchCondition; //текущее состояние полного условия поиска
    selectedElement?: any; //выделенный элемент
    parentElement: PredicateCondition | undefined; //родительский предикат для выделенного элемента
    searchCondition: SearchCondition; //текущее состояние блока "id3tag-operation-value"
    name: string //имя для сохранения
}

interface NullCondition extends IBaseSearchCondition {
    asdf: any
}

/**
 * TODO!!! Кошмар который при этом работает.
 */
class SearchConditionConstructor extends React.Component<SearchConditionConstructorProps, ISearchConditionConstructorState> {


    state = {
        searchCondition: {
            id3Tag: "ARTIST" as Id3Tag,
            operation: 'REGEX' as Operation,
            value: ''
        },
        selectedElement: undefined,
        condition: this.props.editCondition !== undefined ? this.props.editCondition.condition : {asdf: ''},
        parentElement: undefined,
        name: this.props.editCondition !== undefined ? this.props.editCondition.name : ''
    };

    handleId3TagSelectChanged(event: any) {
        let sc = Object.assign({}, this.state.searchCondition, {id3Tag: event.target.value});
        this.setState(Object.assign({}, this.state, {searchCondition: sc}));

    }

    handleOperationSelectChanged(event: any) {
        let sc = Object.assign({}, this.state.searchCondition, {operation: event.target.value});
        this.setState(Object.assign({}, this.state, {searchCondition: sc}));
    }

    handleValue(event: any) {
        let sc = Object.assign({}, this.state.searchCondition, {value: event.target.value});
        this.setState(Object.assign({}, this.state, {searchCondition: sc}));
    }

    handleName(event: any) {
        this.setState(Object.assign({}, this.state, {name: event.target.value}));
    }

    render(): React.ReactElement<any, string | React.JSXElementConstructor<any>> | string | number | {} | React.ReactNodeArray | React.ReactPortal | boolean | null | undefined {
        if (!this.props.show) return <></>;
        return <>
            <Modal.Dialog>
                <Modal.Header>
                    <Modal.Title><FormGroup className="row">
                        <FormGroup className="col col-sm-9">
                            Edit/create new search condition
                        </FormGroup>
                        {/*<FormGroup className="col col-sm-3">name of search</FormGroup>*/}
                        <FormGroup className="col col-sm-3">
                            <FormControl componentClass="input" onChange={this.handleName.bind(this)}
                                         value={this.state.name} placeholder="enter name of search"/>
                        </FormGroup>
                    </FormGroup></Modal.Title>

                </Modal.Header>

                <Modal.Body>
                    <Label>choose operation</Label><br/>
                    {this.printPreditcateBlock()}
                    <br/>
                    <Label>or enter compare condition</Label><br/>
                    {this.printCompareBlock()}
                    <hr/>
                    <Label>click to choose where set</Label>
                    <ul className="nobull">
                        {this.printCondition(this.state.condition)}
                    </ul>
                    <hr></hr>
                    <Button disabled={this.state.name.length === 0 || !this.isValid(this.state.condition)}
                            onClick={() => this.props.saveChanges(this.state.name, this.state.condition)}>Save</Button>
                    <Button onClick={() => this.props.cancelEdit()}>Cancel</Button>
                </Modal.Body>
            </Modal.Dialog>
        </>
            ;
    }

    private addSearchCondition() {
        if (this.state.parentElement === undefined) {
            this.setState(Object.assign({}, this.state, {
                condition: this.state.searchCondition,
                selectedElement: undefined
            }));
        } else {
            let parentElement: PredicateCondition = this.state.parentElement!;
            let index = parentElement.conditions.findIndex(value => value === this.state.selectedElement);
            parentElement.conditions[index] = this.state.searchCondition;
            this.setState(Object.assign({}, this.state, {parentElement: undefined, selectedElement: undefined}));
        }
    }

    printLi(element: any, caption: string, parentElement?: PredicateCondition) {
        if (element === this.state.selectedElement) {
            return <div onClick={() => this.setState(Object.assign({}, this.state, {
                selectedElement: undefined,
                parentElement: undefined
            }))}><b>{caption}</b></div>
        }
        return <div onClick={() => this.setState(Object.assign({}, this.state, {
            selectedElement: element,
            parentElement: parentElement
        }))}>{caption}</div>
    }

    printCondition(condition?: any, parentElement?: PredicateCondition) {
        if (condition === undefined) return <li>{this.printLi(condition, 'null', undefined)}</li>;
        if (condition.asdf !== undefined) return <li>{this.printLi(condition, 'null', parentElement)}</li>;
        if (condition.type) {
            let condition1 = condition as PredicateCondition;
            return <li>{this.printLi(condition1, condition1.type, parentElement)}
                <ul className="nobull">
                    {condition1.conditions.map((value: any, key) => <div key={key}> { this.printCondition(value, condition1)} </div>)}
                </ul>
            </li>
        }
        return <li>{this.printLi(condition, condition.id3Tag + " " + condition.operation + " " + condition.value, parentElement)}</li>;
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
                let parentElement: PredicateCondition = this.state.parentElement!;
                if (parentElement.type === type) {
                    let parentConditions = parentElement.conditions;
                    parentConditions.push({asdf: 'a'});
                    this.setState(Object.assign({}, this.state, {
                        parentElement: parentElement
                    }));
                    return;
                }
                newCondition = {type: type, conditions: [{asdf: 'a'}, {asdf: 'a'}]};
            } else {
                newCondition = {type: 'NOT', conditions: [{asdf: 'a'}]};
            }
            let parentElement: PredicateCondition = this.state.parentElement!;
            let index = parentElement.conditions.findIndex(value => value === this.state.selectedElement);
            parentElement.conditions[index] = newCondition;
            this.setState(Object.assign({}, this.state, {parentElement: undefined, selectedElement: undefined}));
        }
    }

    private addConditionNew(type: PredicateType) {
        if (this.state.selectedElement !== undefined) {
            let selectedElement : any = this.state.selectedElement!;
            if (selectedElement.type !== undefined) {

            }
        }
    }

    private isValid(condition: any): boolean {
        if (condition.asdf !== undefined) {
            return false;
        }
        if (condition.id3Tag !== undefined) {
            return true;
        }
        let conditions: IBaseSearchCondition[] = condition.conditions;
        return conditions
            .map(value => this.isValid(value))
            .reduce((a, b) => a && b, true);
    }

    private printPreditcateBlock() {
        return <ButtonGroup aria-label="Basic example">
            <Button disabled={this.state.selectedElement === undefined}
                    onClick={() => this.addCondition('NOT')}>NOT</Button>
            <Button disabled={this.state.selectedElement === undefined}
                    onClick={() => this.addCondition('OR')}>OR</Button>
            <Button disabled={this.state.selectedElement === undefined}
                    onClick={() => this.addCondition('AND')}>AND</Button>
        </ButtonGroup>
    }

    private printCompareBlock() {
        return <FormGroup className="row">
            <FormGroup className="col col-sm-3">
                <FormControl
                    componentClass="select" value={this.state.searchCondition.id3Tag}
                    onChange={this.handleId3TagSelectChanged.bind(this)}>
                    <option value="ARTIST">Artist</option>
                    <option value="ALBUM">Album</option>
                    <option value="TITLE">Title</option>
                    <option value="ALBUM_ARTIST">Album artist</option>
                    <option value="GENRE">Genre</option>
                </FormControl>
            </FormGroup>
            <FormGroup className="col col-sm-3">
                <FormControl componentClass="select" value={this.state.searchCondition.operation}
                             onChange={this.handleOperationSelectChanged.bind(this)}>
                    <option value="START_WITH">starts with</option>
                    <option value="CONTAINS">contains</option>
                    <option value="REGEX">regex</option>
                </FormControl>
            </FormGroup>
            <FormGroup className="col col-sm-3">
                <FormControl componentClass="input" onChange={this.handleValue.bind(this)}
                             value={this.state.searchCondition.value}/>
            </FormGroup>
            <FormGroup className="col col-sm-3">
                <Button
                    disabled={this.state.selectedElement === undefined || this.state.searchCondition.value.length === 0}
                    onClick={() => {
                        this.addSearchCondition()
                    }}>add</Button>
            </FormGroup>
        </FormGroup>
    }
}

export default connect(mapStateToProps, matDispatchToProps)(SearchConditionConstructor);