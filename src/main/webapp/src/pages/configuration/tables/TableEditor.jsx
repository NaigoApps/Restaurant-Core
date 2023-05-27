import React from 'react';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import Modal from '../../../widgets/Modal';
import TextEditor from '../../../inputs/TextEditor';
import Row from '../../../widgets/Row';
import Column from '../../../widgets/Column';
import useNetwork from '../../../utils/useNetwork';
import useRemote from '../../../utils/useRemote';
import Button from '../../../widgets/Button';
import ConfirmButton from '../../../widgets/ConfirmButton';

export default function TableEditor({
  onClose,
  table: tableUuid,
  refresh: refreshAll
}) {
  const {
    put,
    remove
  } = useNetwork();

  const [table, refreshThis] = useRemote(`restaurant-tables/${tableUuid}`);

  async function updateProperty(name, value) {
    await put(`restaurant-tables/${tableUuid}/${name}`, { value });
    refreshAll();
    refreshThis();
  }

  async function deleteTable() {
    const result = await remove(`restaurant-tables/${tableUuid}`);
    if (result !== null) {
      refreshAll();
      onClose();
    }
  }

  if (!table) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Column spaced grow>
        <Row>
          <Column grow>
            <TextEditor
              label="Nome"
              initialValue={table.name}
              onConfirm={value => updateProperty('name', value)}
            />
          </Column>
        </Row>
        <Row spaced>
          <Column grow>
            <ConfirmButton
              confirmMessage="Eliminare il tavolo?"
              text="Elimina"
              icon={faTrash}
              kind="danger"
              onClick={deleteTable}
            />
          </Column>
          <Column grow>
            <Button text="Chiudi" kind="info" onClick={onClose}/>
          </Column>
        </Row>
      </Column>
    </Modal>
  );
}
