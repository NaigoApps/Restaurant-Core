import React, {Component} from 'react';
import Button from "../../../widgets/Button";
import {ApplicationActions} from "../../../actions/ApplicationActions";
import Row from "../../../widgets/Row";
import Column from "../../../widgets/Column";

export default class PercentEditor extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let options = this.props.options;
        let value = !!options.value ? options.value.toString() : "0";
        let text = <Row>
            <Column auto justify="center"><span><b>{options.label}</b>:</span></Column>
            <Column><span className="text-left">{value}%</span></Column>
        </Row>;
        return <Button
            disabled={this.props.disabled}
            type={this.props.type}
            highPadding
            text={text}
            commitAction={() => ApplicationActions.showPercentInput(options)}
        />;
    }
}