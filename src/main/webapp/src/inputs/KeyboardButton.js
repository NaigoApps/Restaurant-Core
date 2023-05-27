import React, {Component} from 'react';
import {uuid} from "../../../utils/Utils";
import Button from "../../../widgets/Button";
import Column from "../../../widgets/Column";

export const SIZES = {
    XXSMALL: "XXSMALL",
    XSMALL: "XSMALL",
    SMALL: "SMALL",
    MEDIUM: "MEDIUM",
    HUGE: "HUGE",
    SPACE: "SPACE"
};

export default class KeyboardButton extends Component {
    constructor(props) {
        super(props);
        this.state = {
            uuid: "kbtn_" + uuid()
        }
    }

    computeStyle() {
        let style = ["keyboard-character"];
        switch (this.props.size) {
            case SIZES.XXSMALL:
                style.push("xxs");
                break;
            case SIZES.XSMALL:
                style.push("xs");
                break;
            case SIZES.SMALL:
                style.push("sm");
                break;
            case SIZES.MEDIUM:
                style.push("md");
                break;
            case SIZES.HUGE:
                style.push("lg");
                break;
            case SIZES.SPACE:
                style.push("space");
                break;
            default:
                style.push("xs");
                break;
        }
        return style.join(" ");
    }

    render() {
        const disabled = !!this.props.disabled;
        let style = this.computeStyle();
        let c = this.props.char;
        return <Column customCss={style}>
            <Button
                key={this.state.uuid}
                type={this.props.type}
                text={this.props.text || c}
                icon={this.props.icon}
                disabled={disabled}
                commitAction={() => this.props.onClick(c)}
                fill
            />
        </Column>;
    }

}