import React, {Component} from 'react';
import Column from "./Column";

export default class ColumnSeparator extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        return <Column auto/>;
    }

}