import * as React from 'react';
import {CheckedUncheckedElement} from 'components/CheckedUncheckedElement';
import {MenuItem, NavDropdown} from "react-bootstrap";

export interface DummyConnectionProps {
    // loading: any;
    checked: boolean;
    click: Function;
};

const DummyConnection = (props: DummyConnectionProps) => <NavDropdown title="Connection" id="basic-nav-dropdown">
    <MenuItem onClick={() => props.click()}><CheckedUncheckedElement checked={props.checked}>Connected</CheckedUncheckedElement></MenuItem>
</NavDropdown>;

export default DummyConnection;