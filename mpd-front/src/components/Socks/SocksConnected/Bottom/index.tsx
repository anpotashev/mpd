import * as React from 'react';
import { Tree } from '../Tree';
import SplitterLayout from 'react-splitter-layout';
// import StoredPlaylists from 'src/components/StoredPlaylists';
import { Playlist } from '../Playlist';
import { LeftPanel, LeftPanelElement } from '../LeftPanel';
import 'react-splitter-layout/lib/index.css';
import {StoredPlaylists} from "../StoredPlaylists";
import SearchComponent from "../Search";
import SimpleSearch from "../SimpleSearchComponent";

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
    <LeftPanelElement id="third" title="Stored search" iconClass="glyphicon glyphicon-search">
        <SearchComponent/>
    </LeftPanelElement>
    <LeftPanelElement id="fourth" title="Quick search (beta)" iconClass="glyphicon glyphicon-search">
        <SimpleSearch/>
    </LeftPanelElement>
</LeftPanel>;