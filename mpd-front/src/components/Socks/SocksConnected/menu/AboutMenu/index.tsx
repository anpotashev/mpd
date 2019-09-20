import * as React from 'react';
import {MenuItem, NavDropdown} from "react-bootstrap";

export const AboutMenu = () => {
    return <NavDropdown title="About" id="basic-nav-dropdown">
        <MenuItem href={'https://github.com/anpotashev/mpd'} target="_blank">project on github</MenuItem>
    </NavDropdown>;

};