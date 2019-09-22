import * as React from 'react';
import { Tree } from '../Tree';
import SplitterLayout from 'react-splitter-layout';
// import StoredPlaylists from 'src/components/StoredPlaylists';
import { Playlist } from '../Playlist';
import { LeftPanel, LeftPanelElement } from '../LeftPanel';
import 'react-splitter-layout/lib/index.css';
import {StoredPlaylists} from "../StoredPlaylists";
import SearchComponent from "../Search";

export const Bottom = () => <>
    <SplitterLayout
         vertical={false} percentage={true}
         secondaryInitialSize={80}
    >
        <div>{left()}</div>
        <Playlist/>
    </SplitterLayout></>;

const left = () => <LeftPanel>
    <LeftPanelElement id="first" title="Tree" default={true}  iconClass="glyphicon glyphicon-list-alt">
        <Tree />
    </LeftPanelElement>
    <LeftPanelElement id="second" title="Playlists" iconClass="glyphicon glyphicon-th-list">
        <StoredPlaylists/>
    </LeftPanelElement>
    <LeftPanelElement id="third" title="Search" iconClass="glyphicon glyphicon-search">
        <SearchComponent/>
    </LeftPanelElement>
</LeftPanel>;