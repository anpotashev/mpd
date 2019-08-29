import * as React from 'react';
// import {Nav, NavDropdown } from 'react-bootstrap';
import { LOADING } from 'constants/other';
import Loading from 'components/Loading';
import { CheckedUncheckedElement } from 'components/CheckedUncheckedElement';
import {Nav, NavDropdown} from "react-bootstrap";

export interface DummyConnectionProps {
  // loading: any;
  // checked: boolean;
  // click: any;
};

const loaded = () => <Nav>
<NavDropdown.Item title="Connection" id="connection-dropdown">
{/*<CheckedUncheckedElement*/}
    {/*checked={checked}>Connect</CheckedUncheckedElement>*/}
</NavDropdown.Item></Nav>;
//     </Nav>;
//
// const notLoading = () => <Nav pullRight={false}>
// <NavDropdown title="Connection" id="123">
//     <MenuItem>Not Loading</MenuItem>
// </NavDropdown>
// </Nav>;
//
// const loading = () => <Nav pullRight={false}>
// <NavDropdown title="Connection" id="123">
//     <MenuItem><Loading/></MenuItem>
//     </NavDropdown>
//     </Nav>;

const DummyConnection = (props: DummyConnectionProps) => {
  // switch (props.loading) {
  //   case LOADING.notLoading:
  //     return notLoading();
  //   case LOADING.loading:
  //     return loading();
  //   default:
        return loaded();
  // }
};

export default DummyConnection;