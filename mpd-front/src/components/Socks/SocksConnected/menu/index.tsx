import * as React from 'react';
import {Nav, Navbar,} from 'react-bootstrap';
import {ConnectionMenu} from "./ConnectionMenu";
import {connect} from "react-redux";
import {StreamingMenu} from "./StreamMenu";

interface IMenuPanelProps {
    connected: boolean;
}

const mapStateToProps = (state: any) => {
    return {
        connected: state.mpdConnection.connected,
    }
};
export const MenuComponent = (props: IMenuPanelProps) => <Navbar>
    <Navbar.Brand href="#home">Mpd</Navbar.Brand>
    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
    <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
            <ConnectionMenu/>
            {props.connected && <StreamingMenu/>}
        </Nav>
    </Navbar.Collapse>
</Navbar>;

export const Menu = connect(mapStateToProps)(MenuComponent);