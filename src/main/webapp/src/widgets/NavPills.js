import React, {Component} from 'react';
import Row from "./Row";

export default class NavPills extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Row>
                {this.props.children}
            </Row>
        );
    }

}