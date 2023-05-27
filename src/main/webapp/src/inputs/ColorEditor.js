import React, {Component} from 'react';
import {ApplicationActions} from "../../../actions/ApplicationActions";
import Column from "../../../widgets/Column";
import Row from "../../../widgets/Row";
import ColorButton from "../../../widgets/ColorButton";

/**
 * Events:
 * - onSelect
 * - onDeselect
 * - onSelectPage
 *
 * - onShowModal
 * - onAbort
 * - onConfirm
 */

export default class ColorEditor extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const options = this.props.options;
        let value = options.value;

        let text = <Row>
            <Column auto justify="center">
                <span><b>{options.label}</b>:</span>
            </Column>
            <Column><span className="text-left" >{value.toHexString()}</span></Column>
        </Row>;

        return <ColorButton
            highPadding
            color={value}
            text={text}
            disabled={this.props.disabled}
            textRows={this.props.textRows}
            commitAction={() => ApplicationActions.showColorInput(options)}
        />;
    }

}