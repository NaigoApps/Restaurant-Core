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
import FloatEditor from '../../../inputs/FloatEditor';

export default function DishEditor({
  navigate,
  onClose,
  dish: dishUuid,
  refresh: refreshAll,
}) {
  const {
    put,
    remove
  } = useNetwork();

  const [dish, refreshThis] = useRemote(`dishes/${dishUuid}`);

  async function updateProperty(name, value) {
    await put(`dishes/${dishUuid}/${name}`, { value });
    refreshAll();
    refreshThis();
  }

  async function updateCategory(value) {
    await put(`dishes/${dishUuid}/category`, { value });
    await refreshThis();
    navigate(`../${value}`);
  }

  async function deleteDish() {
    const result = await remove(`dishes/${dishUuid}`);
    if (result !== null) {
      refreshAll();
      onClose();
    }
  }

  if (!dish) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Column spaced>
        <Row>
          <Column grow>
            <TextEditor
              label="Nome"
              initialValue={dish.name}
              onConfirm={value => updateProperty('name', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <TextEditor
              label="Descrizione"
              initialValue={dish.description}
              onConfirm={value => updateProperty('description', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <FloatEditor
              label="Prezzo"
              initialValue={dish.price}
              onConfirm={value => updateProperty('price', value)}
              currency
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <RemoteSelectEditor
              url="dishes-statuses"
              label="Stato"
              value={dish.status}
              onSelectOption={value => updateProperty('status', value)}
            />
          </Column>
        </Row>
        <Row>
          <Column grow>
            <RemoteSelectEditor
              url="categories"
              text={cat => cat.name}
              id={cat => cat.uuid}
              label="Categoria"
              value={dish.categoryId}
              onSelectOption={value => updateCategory(value.uuid)}
              valueId
            />
          </Column>
        </Row>
        <Row spaced>
          <Column grow>
            <ConfirmButton
              confirmMessage="Eliminare il piatto?"
              text="Elimina"
              icon={faTrash}
              kind="danger"
              onClick={deleteDish}
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
