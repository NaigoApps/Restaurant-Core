import React, {Component} from 'react';
import {ApplicationActions} from "../actions/ApplicationActions";
import NavElement from "./NavElement";
import {CONFIGURATION, Pages} from "../App";

export default class NavConfigurationButton extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <NavElement
                key="conf"
                text="Configurazione"
                active={this.props.active}
                commitAction={() => ApplicationActions.goToPage(Pages.CONFIGURATION)}
            />
        );
    }

}