import { faTrash } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import IntegerEditor from '../../../inputs/IntegerEditor';
import TextEditor from '../../../inputs/TextEditor';
import useNetwork from '../../../utils/useNetwork';
import useRemote from '../../../utils/useRemote';
import Button from '../../../widgets/Button';
import Column from '../../../widgets/Column';
import ConfirmButton from '../../../widgets/ConfirmButton';
import Modal from '../../../widgets/Modal';
import Row from '../../../widgets/Row';

export default function PrinterEditor({
  onClose,
  printer: printerUuid,
  refresh: refreshAll,
}) {
  const {
    put,
    remove
  } = useNetwork();

  const [printer, refreshThis] = useRemote(`printers/${printerUuid}`);

  async function updateProperty(name, value) {
    await put(`printers/${printerUuid}/${name}`, { value });
    refreshAll();
    refreshThis();
  }

  async function deletePrinter() {
    const result = await remove(`printers/${printerUuid}`);
    if (result !== null) {
      refreshAll();
      onClose();
    }
  }

  if (!printer) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Column spaced>
        <Row>
          <Column grow>
            <TextEditor
              label="Nome"
              initialValue={printer.name}
              onConfirm={value => updateProperty('name', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <TextEditor
              label="Indirizzo"
              initialValue={printer.address}
              onConfirm={value => updateProperty('address', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <TextEditor
              label="Porta"
              initialValue={printer.port}
              onConfirm={value => updateProperty('port', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <IntegerEditor
              label="Caratteri per riga"
              initialValue={printer.lineCharacters}
              onConfirm={value => updateProperty('lineCharacters', value)}
            />
          </Column>
        </Row>
        <Row spaced>
          <Column grow>
            <ConfirmButton
              confirmMessage="Eliminare la stampante?"
              text="Elimina"
              icon={faTrash}
              kind="danger"
              onClick={deletePrinter}
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
