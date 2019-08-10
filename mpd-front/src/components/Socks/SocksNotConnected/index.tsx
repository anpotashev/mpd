import * as React from 'react';
import {Card} from "react-bootstrap";
import './index.css';

export const SocksNotConnected = () => <Card>
    <Card.Title><h1 className='red'>No connection to server</h1></Card.Title>
    <Card.Body><h3>When the connection is restored, the page will reload itself.</h3></Card.Body>
</Card>;