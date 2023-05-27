import React, {Component} from 'react';

export default class LinesGroup extends Component {
    constructor(props) {
        super(props);
    }

    getClassName() {
        let classes = [];
        classes.push(this.props.vertical ? "lines-group-vertical" : "lines-group");
        return classes.join(" ");
    }

    render() {
        return (
            <div className={this.getClassName()}>
                {this.props.children}
            </div>
        );
    }

}