import React, {Component} from 'react';
import {ApplicationActions} from "../actions/ApplicationActions";
import NavElement from "./NavElement";
import {CONFIGURATION, SETTINGS} from "../App";

export default class NavSettingsButton extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <NavElement
                key="settings"
                text="Impostazioni"
                active={this.props.active}
                commitAction={() => ApplicationActions.goToPage(SETTINGS)}
            />
        );
    }

}