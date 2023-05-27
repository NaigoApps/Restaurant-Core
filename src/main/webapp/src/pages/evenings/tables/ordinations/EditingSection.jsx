import React from 'react';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import FloatEditor from '../../../../inputs/FloatEditor';
import IntegerEditor from '../../../../inputs/IntegerEditor';
import Column from '../../../../widgets/Column';
import Row from '../../../../widgets/Row';
import SelectEditor from '../../../../inputs/SelectEditor';
import Button from '../../../../widgets/Button';
import SelectInput from '../../../../inputs/SelectInput';
import TextEditor from '../../../../inputs/TextEditor';
import useRemote from '../../../../utils/useRemote';

export default function ({
  ordination,
  selectedPhase,
  selectedGroup,
  onDeselectGroup,
  onSelectAddition,
  onSelectQuantity,
  onSelectPhase,
  onSelectPrice,
  onSelectNotes,
}) {
  const { phase } = ordination.orders[selectedPhase];
  const order = ordination.orders[selectedPhase].orders[selectedGroup];

  let [additions] = useRemote(`additions?dish=${order.dish.uuid}`);
  if (!additions) {
    additions = [];
  }

  let [phases] = useRemote('phases');
  if (!phases) {
    phases = [];
  }

  order.additions.forEach((addition) => {
    if (!additions.find(a => a.uuid === addition.uuid)) {
      additions.push(addition);
    }
  });

  return (
    <Row grow>
      <Column>
        <Row grow>
          <Column>
            <Row>
              <Column>
                <p className="h6 has-text-centered">{order.dish.name}</p>
              </Column>
            </Row>
            <Row>
              <Column>
                <TextEditor
                  label="Variante libera"
                  initialValue={order ? order.notes : ''}
                  onConfirm={onSelectNotes}
                />
              </Column>
            </Row>
            <Row>
              <Column>
                <FloatEditor
                  label="Prezzo"
                  initialValue={order ? order.price : 0}
                  onConfirm={onSelectPrice}
                  currency
                />
              </Column>
              <Column>
                <IntegerEditor
                  label="Quantità"
                  initialValue={order ? order.quantity : 0}
                  onConfirm={onSelectQuantity}
                />
              </Column>
              <Column>
                <SelectEditor
                  rows={2}
                  cols={2}
                  label="Portata"
                  options={phases}
                  id={p => p.uuid}
                  text={p => p.name}
                  value={phase}
                  onSelectOption={onSelectPhase}
                />
              </Column>
              <Column size="is-one-fifth">
                <Button kind="danger" icon={faTrash} onClick={() => onSelectQuantity(0)} />
              </Column>
            </Row>
            <Row>
              <Column>
                <SelectInput
                  uuid="order_fixed_additions"
                  rows={4}
                  cols={2}
                  options={additions}
                  text={addition => addition.name}
                  id={a => a.uuid}
                  value={order ? order.additions : []}
                  onSelectOption={onSelectAddition}
                  multiSelect
                  alwaysShowPages
                />
              </Column>
            </Row>
          </Column>
        </Row>
        <Row>
          <Column>
            <Button text="Torna a inserimento" type="info" onClick={() => onDeselectGroup()} />
          </Column>
        </Row>
      </Column>
    </Row>
  );
}
