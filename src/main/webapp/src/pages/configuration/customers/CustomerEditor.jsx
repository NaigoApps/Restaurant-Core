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

export default function CustomerEditor({
  onClose,
  customer: customerUuid,
  refresh: refreshAll,
}) {
  const {
    put,
    remove,
  } = useNetwork();

  const [customer, refreshThis] = useRemote(`customers/${customerUuid}`);

  async function updateProperty(name, value) {
    await put(`customers/${customerUuid}/${name}`, { value });
    refreshAll();
    refreshThis();
  }

  async function deleteCustomer() {
    const result = await remove(`customers/${customerUuid}`);
    if (result !== null) {
      refreshAll();
      onClose();
    }
  }

  if (!customer) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Column spaced grow>
        <Row>
          <Column grow spaced>
            <TextEditor
              label="Nome"
              initialValue={customer.name}
              onConfirm={value => updateProperty('name', value)}
            />
            <TextEditor
              label="Cognome"
              initialValue={customer.surname}
              onConfirm={value => updateProperty('surname', value)}
            />
            <TextEditor
              label="Codice fiscale"
              initialValue={customer.cf}
              onConfirm={value => updateProperty('cf', value)}
            />
            <TextEditor
              label="Partita IVA"
              initialValue={customer.piva}
              onConfirm={value => updateProperty('piva', value)}
            />
            <TextEditor
              label="Città"
              initialValue={customer.city}
              onConfirm={value => updateProperty('city', value)}
            />
            <TextEditor
              label="CAP"
              initialValue={customer.cap}
              onConfirm={value => updateProperty('cap', value)}
            />
            <TextEditor
              label="Indirizzo"
              initialValue={customer.address}
              onConfirm={value => updateProperty('address', value)}
            />
            <TextEditor
              label="Paese"
              initialValue={customer.district}
              onConfirm={value => updateProperty('district', value)}
            />
          </Column>
        </Row>
        <Row spaced>
          <Column grow>
            <ConfirmButton
              confirmMessage="Eliminare il cliente?"
              text="Elimina"
              icon={faTrash}
              kind="danger"
              onClick={deleteCustomer}
            />
          </Column>
          <Column grow>
            <Button text="Chiudi" kind="info" onClick={onClose} />
          </Column>
        </Row>
      </Column>
    </Modal>
  );
}
