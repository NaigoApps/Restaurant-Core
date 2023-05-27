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

export default function LocationEditor({
  onClose,
  location: locationUuid,
  refresh: refreshAll
}) {
  const {
    put,
    remove
  } = useNetwork();

  const [location, refreshThis] = useRemote(`locations/${locationUuid}`);

  async function updateProperty(name, value) {
    await put(`locations/${locationUuid}/${name}`, { value });
    refreshAll();
    refreshThis();
  }

  async function deleteLocation() {
    const result = await remove(`locations/${locationUuid}`);
    if (result !== null) {
      refreshAll();
      onClose();
    }
  }

  if (!location) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Column spaced>
        <Row>
          <Column grow>
            <TextEditor
              label="Nome"
              initialValue={location.name}
              onConfirm={value => updateProperty('name', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <RemoteSelectEditor
              label="Stampante"
              url="printers"
              id={p => p.uuid}
              text={p => p.name}
              value={location.printer}
              onSelectOption={value => updateProperty('printer', value.uuid)}
            />
          </Column>
        </Row>
        <Row spaced>
          <Column grow>
            <ConfirmButton
              confirmMessage="Eliminare la postazione?"
              text="Elimina"
              icon={faTrash}
              kind="danger"
              onClick={deleteLocation}
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
