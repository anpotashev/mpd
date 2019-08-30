import * as React from 'react';
import {CheckedUncheckedElement} from 'components/CheckedUncheckedElement';
import {MenuItem, NavDropdown} from "react-bootstrap";

export interface DummyConnectionProps {
    // requestStatus: any;
    checked: boolean | undefined;
    click: Function;
};

const DummyConnection = (props: DummyConnectionProps) => {
    if (props.checked !== undefined) return <NavDropdown title="Connection" id="basic-nav-dropdown">
        <MenuItem onClick={() => props.click()}><CheckedUncheckedElement
            checked={props.checked}>Connected</CheckedUncheckedElement></MenuItem>
    </NavDropdown>;
    return <></>;
};

export default DummyConnection;