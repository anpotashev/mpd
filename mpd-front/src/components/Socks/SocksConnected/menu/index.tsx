import * as React from 'react';
import { Navbar } from 'react-bootstrap';
import { ConnectionMenu } from './ConnectionMenu';
// import { SettingMenu } from './SettingMenu';
// import { OutputsMenu } from './OutputsMenu';
// import PlayerMenu from './PlayerMenu';

export const Menu = () => <Navbar collapseOnSelect>
        <Navbar.Brand>
            Mpd
    </Navbar.Brand>
    <ConnectionMenu />
    {/*<PlayerMenu/>*/}
    {/*<SettingMenu />*/}
    {/*<OutputsMenu/>*/}
    </Navbar>;