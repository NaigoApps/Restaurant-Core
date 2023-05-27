import React, {Component} from 'react';

export default class EntitySelectInput extends Component {
    constructor(props) {
        super(props);
        this.state = this.resetState(props);
    }

    componentWillReceiveProps(props) {
        this.setState(this.resetState(props));
    }

    resetState(props) {
        return {
            selectedOption: props.default || ""
        }
    }


    onChange(event) {
        if (event.target.value) {
            this.props.commitAction(event.target.value);
        } else {
            this.props.commitAction("");
        }
        this.setState({
            selectedOption: event.target.value
        });
    }

    findByUuid(array, uuid) {
        var result = undefined;
        array.forEach((value) => {
            if (value.uuid === uuid) {
                result = value;
            }
        });
        return result;
    }

    render() {
        const label = this.props.label;
        const nullOption = this.props.nullOption ? (<option></option>) : "";
        const options = this.props.options.map((option, index) =>
            <option key={option.uuid} value={option.uuid}>{this.props.render(option)}</option>
        );
        const field = (
            <select value={this.state.selectedOption} className="form-control"
                    onChange={this.onChange.bind(this)}>
                {nullOption}
                {options}
            </select>);
        return field;
    }

}