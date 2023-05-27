import React, {Component} from 'react';
import Button from "./Button";
import {ApplicationActions} from "../actions/ApplicationActions";

export default class NavElementLink extends Component {
    constructor(props) {
        super(props);
    }

    static getClassName(props){
        let classes = ["nav-link"];
        if(props.active){
            classes.push("active");
        }
        if(props.disabled){
            classes.push("not-allowed");
        }
        return classes.join(" ");
    }

    goToPage(page){
        ApplicationActions.goToPage(page);
    }

    render() {
        const text = this.props.text;
        const disabled = this.props.disabled;

        return (
                <li className="nav-item">
                    <Button
                        text={text}
                        disabled={disabled}
                        commitAction={() => this.goToPage(this.props.page)}
                    />
                </li>
        );
    }

}