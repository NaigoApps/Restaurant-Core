import React, {Component} from 'react';
import Button from "./Button";
import Column from "./Column";

export default class NavElement extends Component {
    constructor(props) {
        super(props);
    }

    clickAction(){
        if(this.props.commitAction && !this.props.disabled){
            this.props.commitAction();
        }
    }

    render() {
        const text = this.props.text;
        const active = this.props.active;
        const disabled = this.props.disabled;
        const type = this.props.type;

        return (
            <Column auto>
                <Button
                    type={type || "secondary"}
                    icon={this.props.icon}
                    text={text}
                    disabled={disabled}
                    active={active}
                    commitAction={() => this.clickAction()}
                />
            </Column>
        );
    }

}