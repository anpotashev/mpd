import * as React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {SocksNotConnected} from "./SocksNotConnected";
import {SocksConnected} from "./SocksConnected";
import {IProps} from "./types";
import * as Actions from "actions";

const mapStateToProps = (state: any) => {
    return {
        socksConnected: state.socksConnection
    }
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        socksConnect: Actions.socksConnect
    }, dispatch);

class Socks extends React.Component<IProps> {

    componentDidMount(): void {
        this.props.socksConnect();
    }

    render() {
        return this.props.socksConnected
            ? <SocksConnected/>
            : <SocksNotConnected/>
    }

}

export default connect(mapStateToProps, mapDispatchToProps)(Socks);