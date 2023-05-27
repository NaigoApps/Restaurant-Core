import { faCheck, faTrash } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import BooleanInput from '../../../inputs/BooleanInput';
import FloatEditor from '../../../inputs/FloatEditor';
import TextEditor from '../../../inputs/TextEditor';
import useNetwork from '../../../utils/useNetwork';
import useRemote from '../../../utils/useRemote';
import Button from '../../../widgets/Button';
import Column from '../../../widgets/Column';
import ConfirmButton from '../../../widgets/ConfirmButton';
import Modal from '../../../widgets/Modal';
import Row from '../../../widgets/Row';

export default function AdditionEditor({
  onClose,
  addition: additionUuid,
  refresh: refreshAll
}) {
  const {
    put,
    remove
  } = useNetwork();

  const [addition, refreshThis] = useRemote(`additions/${additionUuid}`);

  async function updateProperty(name, value) {
    await put(`additions/${additionUuid}/${name}`, { value });
    refreshAll();
    refreshThis();
  }

  async function deleteAddition() {
    const result = await remove(`additions/${additionUuid}`);
    if (result !== null) {
      refreshAll();
      onClose();
    }
  }

  if (!addition) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Column spaced>
        <Row>
          <Column grow>
            <TextEditor
              label="Nome"
              initialValue={addition.name}
              onConfirm={value => updateProperty('name', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <FloatEditor
              label="Prezzo"
              initialValue={addition.price}
              onConfirm={value => updateProperty('price', value)}
              currency
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <BooleanInput
              label="Generica"
              value={addition.generic}
              falseLabel="No"
              trueLabel="Sì"
              onConfirm={value => updateProperty('generic', value.toString())}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <ConfirmButton
              confirmMessage="Eliminare la variante?"
              text="Elimina"
              icon={faTrash}
              kind="danger"
              onClick={deleteAddition}
            />
          </Column>
          <Column grow>
            <Button
              text="Conferma"
              kind="success"
              icon={faCheck}
              onClick={onClose}/>
          </Column>
        </Row>
      </Column>
    </Modal>
  );
}
