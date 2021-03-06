import * as React from 'react';
import { FolderCaption } from './FolderCaption';
import { FileCaption } from './FileCaption';
import './index.css';
import {ITreeElement} from "reducers/Tree";
import {bindActionCreators} from "redux";
import * as Actions from "actions";
import {connect} from "react-redux";

const openClass = 'glyphicon glyphicon-folder-open span-li';
const closedClass = 'glyphicon glyphicon-folder-close span-li';

const getClass = (open: boolean) => open ? openClass : closedClass;

export interface ITreeLevelProps {
    open: boolean;
    element: ITreeElement;
    path: string;
    catpureOject: Function;
}

interface ITreeLevelState {
    open: boolean;
}

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
      catpureOject: Actions.captureObject
    }, dispatch);


class TreeLevel extends React.Component<ITreeLevelProps, ITreeLevelState> {

  state = {
    open: this.props.open
  };

  toggleOpen() {
    var newState = this.state;
    newState.open = !newState.open;
    this.setState(newState);
  }

  render() {
    return <ul className="tree-ul"
               onMouseDown={e => { e.stopPropagation(); this.props.catpureOject({path: this.props.path, type: 'directory'})}}
    >
      {this.printCaption()}
      {this.printChildren()}
    </ul>
  }

  printCaption() {
      let title = this.props.element.directory || "";
    return <li className="tree-li-directory">
        <span className={getClass(this.state.open)}
              onClick={e => this.toggleOpen()}/>
      <FolderCaption
            title={title} path={this.props.path}
        />
    </li>
  }

  printChildren() {
    if (!this.state.open) {
      return <></>;
    }

    return this.props.element.children.map(
        (t: any, key: any) => (t.children !== undefined)
            ? this.printTreeLevel(t, key)
            : this.printFile(t, key));
  }

  printTreeLevel(t: any, key: any) {
    return <TreeLevel key={key}
                      open={false}
                      element={{directory: t.directory, children: t.children}}
                      path={(this.props.path.length > 0
                          ? this.props.path + '/'
                          : '')                          
            + t.directory}
                      catpureOject={this.props.catpureOject}
    />
  }

  printFile(t: any, key: any) {
      return <FileCaption key={key} path={(this.props.path.length === 0) ? t.file : this.props.path + '/' + t.file}
          title={t.file}
    />
  }

}

export default connect(null, mapDispatchToProps)(TreeLevel);
