import React, {Component} from 'react';
import IntKeyPad from "../IntKeyPad";
import Column from "../../../widgets/Column";
import Row from "../../../widgets/Row";

export default class PercentInput extends Component {
    constructor(props) {
        super(props);
    }

    onChar(char) {
        if (this.props.onChar) {
            this.props.onChar(char);
        }
    }

    onChange(evt) {
        if (this.props.onChange) {
            this.props.onChange(evt.target.value);
        }
    }

    render() {
        const text = this.props.text;
        const placeholder = this.props.placeholder;
        const disabled = this.props.disabled;

        return (
            <Row>
                <Column>
                    <Row>
                        <Column>
                            <input
                                id={this.props.uuid}
                                className="form-control"
                                placeholder={placeholder}
                                type="text"
                                disabled={disabled}
                                value={text ? text + "%" : ""}
                                onChange={data => this.onChange(data)}/>
                        </Column>
                    </Row>
                    <Row justify="center" ofList>
                        <Column auto>
                            <IntKeyPad
                                disabled={disabled}
                                onCharAction={char => this.onChar(char)}/>
                        </Column>
                    </Row>
                </Column>
            </Row>);
    }

}