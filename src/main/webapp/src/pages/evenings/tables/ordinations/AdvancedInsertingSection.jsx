import React, { useState } from 'react';
import FloatEditor from '../../../../inputs/FloatEditor';
import IntegerEditor from '../../../../inputs/IntegerEditor';
import SelectInput from '../../../../inputs/SelectInput';
import TextEditor from '../../../../inputs/TextEditor';
import useRemote from '../../../../utils/useRemote';
import Column from '../../../../widgets/Column';
import OkCancelDialog from '../../../../widgets/OkCancelDialog';
import Row from '../../../../widgets/Row';

export default function ({
  dish, onCancel, onOk,
}) {
  const [quantity, setQuantity] = useState(1);
  const [price, setPrice] = useState(dish.price);
  const [notes, setNotes] = useState(null);
  const [additions, setAdditions] = useState([]);

  function onSelectAddition(addition) {
    setAdditions((old) => {
      const index = old.indexOf(addition);
      if (index !== -1) {
        setPrice(p => p - addition.price);
        return old.slice(0, index).concat(old.slice(index + 1, old.length));
      }
      setPrice(p => p + addition.price);
      return old.concat([addition]);
    });
  }

  let [availableAdditions] = useRemote(`additions?dish=${dish.uuid}`);
  if (!availableAdditions) {
    availableAdditions = [];
  }

  return (
    <OkCancelDialog
      size="md"
      visible
      onCancel={onCancel}
      onOk={() => onOk(quantity, price, additions, notes)}
    >
      <Row>
        <Column grow>
          <Row>
            <Column grow>
              <Row>
                <Column grow>
                  <p className="h6 has-text-centered">{dish.name}</p>
                </Column>
              </Row>
              <Row margin="medium">
                <Column grow>
                  <TextEditor
                    label="Variante libera"
                    initialValue={notes}
                    onConfirm={setNotes}
                  />
                </Column>
              </Row>
              <Row margin="medium">
                <Column grow>
                  <FloatEditor
                    label="Prezzo"
                    initialValue={price}
                    onConfirm={setPrice}
                    currency
                  />
                </Column>
                <Column grow>
                  <IntegerEditor
                    label="Quantità"
                    initialValue={quantity}
                    onConfirm={setQuantity}
                  />
                </Column>
              </Row>
              <Row>
                <Column grow>
                  <SelectInput
                    uuid="order_fixed_additions"
                    rows={5}
                    cols={4}
                    options={availableAdditions}
                    text={addition => addition.name}
                    id={a => a.uuid}
                    value={additions}
                    onSelectOption={onSelectAddition}
                    multiSelect
                    alwaysShowPages
                  />
                </Column>
              </Row>
            </Column>
          </Row>
        </Column>
      </Row>
    </OkCancelDialog>
  );
}
