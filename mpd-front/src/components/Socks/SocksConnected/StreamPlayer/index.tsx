import * as React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PlayerController from './PlayerController/index'
import {IStatusReducer} from 'reducers/Status';
import {IStreamReducer} from 'reducers/Stream';
import * as Actions from "actions";
import {LOADING} from "redux/SockJSMiddleware2";

declare var $: any;

const mapStateToProps = (state: any) => {
  return {
    stream: state.stream,
    status: state.status
  }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
      getStreamUrl: Actions.getStreamUrl
    }, dispatch);

export interface IStreamPlayerProps {
  getStreamUrl: Function;
  status: IStatusReducer;
  stream: IStreamReducer;
}

class StreamPlayer extends React.Component<IStreamPlayerProps, any> {
  constructor(props: IStreamPlayerProps) {
    super(props);
    $('#JPlayer').jPlayer({
      ready: function () {
        $("#JPlayer").jPlayer();
      }
    });
  }

  render() {
    switch (this.props.stream.requestStatus) {
      case LOADING.notLoading:
        this.props.getStreamUrl();
        return <></>;
      case LOADING.loading:
        return <></>;
      default:
        var enable = this.props.status.state === "play" && this.props.stream.enableStreaming;
        return enable ? <PlayerController url={this.props.stream.streamUrl}/> : <div/>;
    }
  }
}

export default connect(mapStateToProps, matDispatchToProps)(StreamPlayer)