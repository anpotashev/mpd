import * as React from 'react';
// import * as $ from 'jquery';
// import "jPlayer";
declare var $: any;

export interface JPlayerControllerProps {
    url: string;
}

export default class PlayerController extends React.Component<JPlayerControllerProps, any> {

  componentDidMount() {
    $("#JPlayer").jPlayer("setMedia", {
      mp3: this.props.url
    });
    $("#JPlayer").jPlayer("play");
  }

  componentWillUnmount() {
    $("#JPlayer").jPlayer("clearMedia");
  }

  render() {
    return <></>
  }
}