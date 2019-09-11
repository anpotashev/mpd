import * as React from 'react';

export const LeftPanel = (props: any) => <div className="container">
  <div className="row">
    <div className="col-sm-3 col-md-12">
      <div className="panel-group" id="accordion">
        {props.children}
      </div>
    </div>
  </div>
</div>;

export const LeftPanelElement = (props: any) => <div className="panel panel-default">
  <div className="panel-heading">
    <h4 className="panel-title">
      <a data-toggle="collapse" data-parent="#accordion"
         href={"#" + props.id}>{props.title}</a>
    </h4>
  </div>
  <div id={props.id}
       className={"panel-collapse collapse" + (props.default ? ' in' : '')}>
    <div className="panel-body">
      {props.children}
    </div>
  </div>
</div>;