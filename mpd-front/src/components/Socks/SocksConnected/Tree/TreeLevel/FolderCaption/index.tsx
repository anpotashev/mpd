import * as React from 'react';

export interface IFolderCaptionProps {
    path: string;
    title: string;
    // addDirToPlaylistFirst: any;
    addDirToPlaylist: Function;
    // updateDb: any;
}

export const FolderCaption = (props: IFolderCaptionProps) => <span className="dropdown">
    <a className="dropdown-toggle" role="button"
        data-toggle="dropdown">{props.title}</a>
    <ul className="dropdown-menu">
        <li><a className="dropdown-item">add to current playlist at first</a></li>
        <li><a className="dropdown-item"
               onClick={e => props.addDirToPlaylist(props.path)}>add to current playlist at last</a></li>
        {/*<li><a className="dropdown-item">add to current playlist at last</a></li>*/}
        <li><a className="dropdown-item">update music database (full)</a></li>
        <li><a className="dropdown-item">update music database (from here)</a></li>
        {/*<li><a className="dropdown-item"*/}
        {/*onClick={e => props.addDirToPlaylistFirst(props.path)}>add to current playlist at first</a></li>*/}
        {/*<li><a className="dropdown-item"*/}
            {/*onClick={e => props.updateDb('')}>update music database (full)</a></li>*/}
        {/*<li><a className="dropdown-item"*/}
            {/*onClick={e => props.updateDb(props.path)}>update music database (from here)</a></li>*/}
    </ul>
</span>;
