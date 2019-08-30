import * as React from 'react';

export interface IFileCaptionProps {
    path: string;
    title: string;
    // addFileToPlaylist: any;
    // addFileToPlaylistFirst: any;
}

export const FileCaption = (props: IFileCaptionProps) => <li className="tree-li-file"><span
    className="glyphicon glyphicon-file"/>
  <span className="dropdown">
        <a className="dropdown-toggle" role="button"
           data-toggle="dropdown">{props.title}</a>
        <ul className="dropdown-menu">
            <li><a className="dropdown-item">add to current playlist at first</a></li>
            <li><a className="dropdown-item">add to current playlist at last</a></li>
            {/*<li><a className="dropdown-item"*/}
                {/*onClick={e => props.addFileToPlaylistFirst(props.path)}>add to current playlist at first</a></li>*/}
            {/*<li><a className="dropdown-item"*/}
                {/*onClick={e => props.addFileToPlaylist(props.path)}>add to current playlist at last</a></li>*/}
        </ul>
      </span>
</li>;

