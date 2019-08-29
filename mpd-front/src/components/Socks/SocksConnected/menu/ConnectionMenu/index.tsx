import * as React from 'react';
import { connect } from 'react-redux';
import * as Actions from 'actions';
import { bindActionCreators } from 'redux';
import DummyConnection from "./dummy";

const mapStateToProps = (state: any) => {
  return {
    connection: state.connection
  }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
      sendMessage: Actions.sendMessage
    }, dispatch);

const Connection = (props: any) => <DummyConnection
    // loading={props.connection.loading}
// checked={props.connection.connected}
// click={() => props.changeConnectState(props.connection.connected)}
/>;

export const ConnectionMenu = connect(mapStateToProps, matDispatchToProps)(Connection);