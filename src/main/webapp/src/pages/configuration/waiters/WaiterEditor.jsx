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
import RemoteSelectEditor from '../../../inputs/RemoteSelectEditor';

export default function WaiterEditor({
  onClose,
  waiter: waiterUuid,
  refresh: refreshAll
}) {
  const {
    put,
    remove
  } = useNetwork();

  const [waiter, refreshThis] = useRemote(`waiters/${waiterUuid}`);

  async function updateProperty(name, value) {
    await put(`waiters/${waiterUuid}/${name}`, { value });
    refreshAll();
    refreshThis();
  }

  async function deleteWaiter() {
    const result = await remove(`waiters/${waiterUuid}`);
    if (result !== null) {
      refreshAll();
      onClose();
    }
  }

  if (!waiter) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Column spaced>
        <Row>
          <Column grow>
            <TextEditor
              label="Nome"
              initialValue={waiter.name}
              onConfirm={value => updateProperty('name', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <TextEditor
              label="Cognome"
              initialValue={waiter.surname}
              onConfirm={value => updateProperty('surname', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <TextEditor
              label="Codice fiscale"
              initialValue={waiter.cf}
              onConfirm={value => updateProperty('cf', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <RemoteSelectEditor
              url="waiter-statuses"
              label="Stato"
              value={waiter.status}
              onSelectOption={value => updateProperty('status', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <ConfirmButton
              confirmMessage="Eliminare il cameriere?"
              text="Elimina"
              icon={faTrash}
              kind="danger"
              onClick={deleteWaiter}
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
