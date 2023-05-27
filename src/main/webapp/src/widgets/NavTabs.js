import React, {Component} from 'react';

export default class NavTabs extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <ul className="nav nav-tabs">
                {this.props.children}
            </ul>
        );
    }

}