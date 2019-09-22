import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import './index.css';
import * as Actions from 'actions';
import {IShortStatusReducer} from "reducers/ShortStatus";
import Loading from "../../../Loading";

interface ProgressProps {
    shortStatus: IShortStatusReducer;
    seek: Function;
    requestShortStatus: Function;
}

interface ProgressState {
    tooltipText: string;
}

const mapStateToProps = (state: any) => {
    return {
        shortStatus: state.shortStatus
    }
};

const matDispatchToProps = (dispatch: any) => bindActionCreators(
    {
        seek: Actions.playerSeek,
        requestShortStatus: Actions.requestShortStatus
    }, dispatch);

class Progress extends React.Component<ProgressProps, ProgressState> {

    state: ProgressState = {
        tooltipText: ''
    };

    private myInput = React.createRef<HTMLDivElement>();

    stoppedComponent() {
        return <div className="progress"
            ref={this.myInput}>
            <div className="progress-bar" role="progressbar" aria-valuenow={0}
                aria-valuemin={0} aria-valuemax={100}
                style={{
                    width: "0px"
                }}>
            </div>
        </div>
    }

    activeComponent(isPaused: boolean) {
        let className = isPaused ? 'progress-bar paused' : 'progress-bar';
        let width = this.props.shortStatus.songTime.current / this.props.shortStatus.songTime.full * 100 + '%';
        return <div className="progress"
            ref={this.myInput}
            onMouseMove={(e) => this.targetMove(e, this.props.shortStatus.songTime.full)}
            onClick={(e) => this.targetClick(e, this.props.shortStatus.songTime.full)}
            data-toggle="tooltip"
            data-placement="bottom"
            title={this.state.tooltipText}
        >
                <span className="progress-value">{this.sec2time(this.props.shortStatus.songTime.current)} / {this.sec2time(
                this.props.shortStatus.songTime.full)}</span>
            <div className={className} role="progressbar"
                    aria-valuenow={this.props.shortStatus.songTime.current}
                    aria-valuemin={0} aria-valuemax={this.props.shortStatus.songTime.full}
                    style={{
                        width: width
                    }} />
        </div>
    }

    targetMove(e: any, songLength: any) {
        if (this.myInput.current) {
            var compWidth = this.myInput.current.offsetWidth;
            var clickWidth = e.nativeEvent.offsetX;
            var newPosition = Math.round(songLength / compWidth * clickWidth);

            this.setState({ tooltipText: 'Click to seek to\n' + this.sec2time(newPosition)});
        }
    }

    targetClick(e: any, songLength: any) {
        if (this.myInput.current) {
            var compWidth = this.myInput.current.offsetWidth;
            var clickWidth = e.nativeEvent.offsetX;
            var newPosition = Math.round(songLength / compWidth * clickWidth);
            this.props.seek(this.props.shortStatus.songPos, newPosition);
        }
    }


    render() {
        return <Loading request={this.props.requestShortStatus} state={this.props.shortStatus}>
            {this.inside()}
        </Loading>
    }

    inside() {
        if (this.props.shortStatus.songTime === null || this.props.shortStatus.songTime.full <= 0) {
            return this.stoppedComponent();
        }
        if (!this.props.shortStatus.playing) {
            return this.activeComponent(true);
        }
        return this.activeComponent(false);
    }

    sec2time(timeInSeconds : number) {
        let pad = function (num: any, size: any) {
            return ('000' + num).slice(size * -1);
        };
        let time : number = parseFloat(timeInSeconds.toFixed(3));
        let hours = Math.floor(time / 60 / 60);
        let minutes = Math.floor(time / 60) % 60;
        let seconds = Math.floor(time - minutes * 60);

        return ((hours !== 0) ? hours + ':' : '') + pad(minutes, 2) + ':' + pad(
            seconds, 2);
    }
}

export default connect(mapStateToProps, matDispatchToProps)(Progress );

