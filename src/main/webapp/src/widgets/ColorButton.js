import React, {Component} from 'react';

/**
 * Expects:
 * - name: label caption
 * - changeAction: action to throw on change
 */

export default class ColorButton extends Component {
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
        return classes.join(" ");
    }

    render() {
        let content = <div style={{
            backgroundColor: this.props.color.toHexString(),
            color: this.props.color.foreground().toHexString(),
            padding: "5px"
        }}/>;

        const style = {};
        if(this.props.active){
            style["border"] = "1px solid green";
        }

        return (
            <button
                type="button"
                style={style}
                className={this.getClassName()}
                onMouseUp={this.clickAction.bind(this)}
                disabled={!!this.props.disabled}>
                {content}
            </button>
        );
    }

}