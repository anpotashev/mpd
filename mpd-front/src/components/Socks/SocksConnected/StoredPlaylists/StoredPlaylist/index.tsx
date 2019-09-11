import * as React from 'react';
import {PlaylistCaption} from "../PlaylistCaption";
import {IPlaylistItem} from "reducers/Playlist";
import {FileCaption} from "../../Tree/TreeLevel/FileCaption";
import 'components/Socks/SocksConnected/Tree/TreeLevel/index.css';

export interface IStoredPlaylistProps {
    title: string;
    items: IPlaylistItem[];
}

interface IStoredPlaylistState {
    open: boolean;
}

const openClass = 'glyphicon glyphicon-folder-open span-li';
const closedClass = 'glyphicon glyphicon-folder-close span-li';

const getClass = (open: boolean) => open ? openClass : closedClass;

export default class StoredPlaylist extends React.Component<IStoredPlaylistProps, IStoredPlaylistState> {

    state: IStoredPlaylistState = {
        open: false
    };

  render = () => <>
    {this.printCaption()}
    {this.state.open && this.printChildren()}
  </>;

  printCaption = () => <li className="tree-li-directory">
        <span className={getClass(this.state.open)}
          onClick={e => this.setState({ open: !this.state.open }) } /><PlaylistCaption title={this.props.title}
        /></li>;

    printChildren = () => this.props.items.map((t: any, key: any) => <FileCaption path={t.file} title={(t.title) ? t.title : t.file} key={key}
    />);
}
