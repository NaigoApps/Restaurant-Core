import React, {Component} from 'react';
import Column from "./Column";
import Row from "./Row";

export default class FormattedParagraph extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let left = this.props.leftText;
        let right = this.props.rightText;

        if (this.props.leftBold) {
            left = <b>{left}</b>;
        }

        let leftColumn = null;
        if (this.props.leftRight) {
            leftColumn = <Column right>{left}</Column>;
        } else {
            leftColumn = <Column left>{left}</Column>;
        }
        return (
            <Row textColor={this.props.textColor} bgColor={this.props.bgColor} bgBlink={this.props.bgBlink} align="center">
                {leftColumn}
                <Column auto>{right}</Column>
            </Row>
        );
    }

}