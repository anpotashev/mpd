import * as React from 'react';
import {Nav, Navbar,} from 'react-bootstrap';
import {ConnectionMenu} from "./ConnectionMenu";

export const Menu = () => <Navbar>
    <Navbar.Brand href="#home">Mpd</Navbar.Brand>
    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
    <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
            <ConnectionMenu/>
        </Nav>
    </Navbar.Collapse>
</Navbar>;