import * as React from 'react';
import { Tree } from '../Tree';
import SplitterLayout from 'react-splitter-layout';
// import StoredPlaylists from 'src/components/StoredPlaylists';
import { Playlist } from '../Playlist';
import { LeftPanel, LeftPanel_Element } from '../LeftPanel';
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
    <LeftPanel_Element id="first" title="Tree" default={true}>
        <Tree />
    </LeftPanel_Element>
    <LeftPanel_Element id="second" title="Playlists">
        {/*<StoredPlaylists/>*/}
    </LeftPanel_Element>
</LeftPanel>;