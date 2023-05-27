import React, {Component} from 'react';
import Row from "./Row";
import Column from "./Column";

/**
 * Expects:
 * - name: label caption
 * - changeAction: action to throw on change
 */

export default class FormattedButton extends Component {
    constructor(props) {
        super(props);
    }

    clickAction() {
        if (this.props.commitAction) {
            this.props.commitAction();
        }
    }

    getClassName() {
        let classes = ["btn"];
        if (this.props.type && !this.props.active) {
            classes.push("btn-" + this.props.type)
        } else if (this.props.active) {
            classes.push("btn-primary");
        } else {
            classes.push("btn-secondary");
        }
        if (this.props.size) {
            classes.push("btn-" + this.props.size)
        }
        return classes.join(" ");
    }

    render() {
        let left = this.props.leftText;
        let right = this.props.rightText;
        let icon = this.props.icon;

        return (
            <button
                type="button"
                className={this.getClassName()}
                onClick={this.clickAction.bind(this)}
                disabled={!!this.props.disabled}>
                <Row justify="between">
                    <Column left>{left}</Column>
                    <Column right auto>{right}</Column>
                </Row>
            </button>
        );
    }

}