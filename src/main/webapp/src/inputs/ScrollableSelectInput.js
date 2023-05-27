import React, {Component} from 'react';
import {distribute, uuid} from "../../../utils/Utils";
import Row from "../../../widgets/Row";
import ColumnSpace from "../../../widgets/ColumnSpace";
import Column from "../../../widgets/Column";
import RenderingData from "./RenderingData";
import TextButton from "../../../widgets/TextButton";
import Scrollable from "../Scrollable";

export default class ScrollableSelectInput extends Component {
    constructor(props) {
        super(props);
    }

    select(option) {
        if (!option || this.isSelected(this.id(option)) && this.props.onDeselect) {
            this.props.onDeselect(this.id(option));
        } else if (this.props.onSelect) {
            this.props.onSelect(this.id(option));
        }
    }

    id(option) {
        if (option && this.props.id) {
            return this.props.id(option);
        }
        return option;
    }

    isSelected(uuid) {
        if (!this.props.multiSelect) {
            return this.props.selected === uuid;
        } else {
            return this.props.selected.includes(uuid);
        }
    }

    renderOption(option) {
        let renderingData;
        if (this.props.renderer) {
            renderingData = this.props.renderer(option);
            if (renderingData instanceof RenderingData) {
                return renderingData;
            }
            renderingData = new RenderingData(renderingData, null);
        } else {
            renderingData = new RenderingData(option, null);
        }
        return renderingData;
    }

    render() {
        const entities = this.props.options;

        const cols = this.props.cols || 3;

        let optionsList;

        optionsList = distribute(entities, cols);
        optionsList = optionsList.map((row, rowIndex) => {
            let buttons = row.map(option => {
                const color = this.props.color ? this.props.color(option) : null;
                const bg = this.props.colorRenderer ? this.props.colorRenderer(option) : "secondary";
                const renderData = this.renderOption(option);
                if (!renderData.backgroundColor) {
                    renderData.backgroundColor = bg;
                }
                return (
                    <Column key={uuid()}>
                        <TextButton
                            color={color}
                            active={this.isSelected(this.id(option))}
                            backgroundColor={renderData.backgroundColor}
                            text={renderData.text}
                            commitAction={() => this.select(option)}
                        />
                    </Column>
                );
            });

            while (buttons.length < cols) {
                buttons.push(<ColumnSpace key={buttons.length}/>);
            }

            return <Row key={rowIndex} ofList={rowIndex > 0}>{buttons}</Row>
        });

        return <Row grow>
            <Column bordered={this.props.bordered}>
                <Row grow>
                    <Column>
                        <Scrollable>
                            {optionsList}
                        </Scrollable>
                    </Column>
                </Row>
            </Column>
        </Row>;
    }

}