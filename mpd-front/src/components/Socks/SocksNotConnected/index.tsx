import * as React from 'react';
import {Button, Card} from "react-bootstrap";
import './index.css';
import {bindActionCreators} from "redux";
import * as Actions from "../../../actions";
import {connect} from "react-redux";

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        socksConnect: Actions.socksConnect
    }, dispatch);

const SocksNotConnectedComponent = () => <Card>
    <Card.Title><h1 className='red'>No connection to server</h1></Card.Title>
    <Card.Body><h3>When the connection is restored, the page will reload itself.</h3>
        <Button onClick={Actions.socksConnect}>or try click this button</Button></Card.Body>

</Card>;
    export const SocksNotConnected = connect(mapDispatchToProps) (SocksNotConnectedComponent);