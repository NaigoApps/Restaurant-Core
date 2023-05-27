import React, {Component} from 'react';

/**
 * Expects:
 * - name: label caption
 * - changeAction: action to throw on change
 */

export default class FlatButton extends Component {
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
        if (this.props.size) {
            classes.push("btn-" + this.props.size)
        } else {
            classes.push("btn-lg")
        }
        if (this.props.fill) {
            classes.push("fill");
        }
        return classes.join(" ");
    }

    render() {
        const style = {
            borderRadius: 0,
            color: this.props.color.foreground().toHexString(),
            backgroundColor: this.props.color.toHexString()
        };

        return (
            <button
                type="button"
                style={style}
                className={this.getClassName()}
                onMouseUp={this.clickAction.bind(this)}
                disabled={!!this.props.disabled}>
                {this.props.active ? "X" : ""}
            </button>
        );
    }

}