import * as React from 'react';
import { Tree } from '../Tree';
import SplitterLayout from 'react-splitter-layout';
// import StoredPlaylists from 'src/components/StoredPlaylists';
import { Playlist } from '../Playlist';
import { LeftPanel, LeftPanelElement } from '../LeftPanel';
import 'react-splitter-layout/lib/index.css';

export const Bottom = () => <>
    <SplitterLayout
         vertical={false} percentage={true}
         secondaryInitialSize={80}
    >
        <div>{left()}</div>
        <Playlist/>
    </SplitterLayout></>;

const left = () => <LeftPanel>
    <LeftPanelElement id="first" title="Tree" default={true}>
        <Tree />
    </LeftPanelElement>
    <LeftPanelElement id="second" title="Playlists">
        {/*<StoredPlaylists/>*/}
    </LeftPanelElement>
</LeftPanel>;