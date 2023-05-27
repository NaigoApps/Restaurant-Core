import React, {Component} from 'react';
import ButtonGroup from "./ButtonGroup";
import Button from "./Button";
import Row from "./Row";
import Column from "./Column";

export default class EntitySelector extends Component {
    constructor(props) {
        super(props);
    }

    selectEntity(value) {
        if (this.props.commitAction) {
            this.props.commitAction(value);
        }
    }

    render() {

        let buttons = this.props.entities.map(entity => {
            return <Button key={entity.get('uuid')}
                           size="lg"
                           active={entity.get('uuid') === this.props.selected}
                           text={this.props.renderer ? this.props.renderer(entity) : "?"}
                           commitAction={this.selectEntity.bind(this, entity.get('uuid'))}/>
        });

        return (
            <Row>
                <Column auto>{this.props.title}</Column>
                <Column>
                    <ButtonGroup>
                        {buttons}
                    </ButtonGroup>
                </Column>
            </Row>
        );
    }

}