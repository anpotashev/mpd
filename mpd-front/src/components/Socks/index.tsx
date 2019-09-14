import * as React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {SocksNotConnected} from "./SocksNotConnected";
import {SocksConnected} from "./SocksConnected";
import * as Actions from "actions";

export interface IProps {
    socksConnected: boolean;
    socksConnect: Function;
    captureObject: Function;
}

const mapStateToProps = (state: any) => {
    return {
        socksConnected: state.socksConnection
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        socksConnect: Actions.playlistRequest,
        captureObject: Actions.captureObject
    }, dispatch);

class Socks extends React.Component<IProps> {

    render() {
        return <div
            onMouseUp={event => this.props.captureObject({path:'', type:'none'})}
        >{this.props.socksConnected
            ? <SocksConnected/>
            : <SocksNotConnected/>}
        </div>
    }

}

export default connect(mapStateToProps, mapDispatchToProps)(Socks);