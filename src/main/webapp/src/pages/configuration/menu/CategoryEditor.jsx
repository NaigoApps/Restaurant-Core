import { faPlus, faTrash } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import ColorInput from '../../../inputs/ColorInput';
import RemoteSelectEditor from '../../../inputs/RemoteSelectEditor';
import SelectInput from '../../../inputs/SelectInput';
import TextEditor from '../../../inputs/TextEditor';
import useNetwork from '../../../utils/useNetwork';
import useRemote from '../../../utils/useRemote';
import Button from '../../../widgets/Button';
import Column from '../../../widgets/Column';
import ConfirmButton from '../../../widgets/ConfirmButton';
import Row from '../../../widgets/Row';
import Wrap from '../../../widgets/Wrap';
import DishEditor from './DishEditor';

export default function CategoryEditor({
  categoryUuid,
  navigate
}) {
  const {
    post,
    put,
    remove
  } = useNetwork();

  const [category, refresh] = useRemote(`categories/${categoryUuid}`);

  const [selectedDish, setSelectedDish] = useState(null);

  async function updateProperty(name, value) {
    await put(`categories/${categoryUuid}/${name}`, { value });
    refresh();
  }

  async function deleteCategory() {
    const result = await remove(`categories/${categoryUuid}`);
    if (result !== null) {
      navigate('..');
    }
  }

  function onSelectAddition(a) {
    const additions = category.additions.map(ad => ad.uuid);
    const index = additions.findIndex(ad => ad === a.uuid);
    if (index === -1) {
      updateProperty('additions', [...additions, a.uuid]);
    } else {
      additions.splice(index, 1);
      updateProperty('additions', additions);
    }
  }

  async function createDish() {
    const result = await post(`dishes?category=${categoryUuid}`);
    if (result != null) {
      refresh();
      setSelectedDish(result);
    }
  }

  function statusColor(status) {
    if (status === 'SUSPENDED') {
      return 'warning';
    }
    if (status === 'REMOVED') {
      return 'danger';
    }
    return 'secondary';
  }

  if (!category) {
    return null;
  }

  return (
    <Column spaced>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">
            {`Categoria ${category.name || '???'}`}
          </p>
        </Column>
        <Column grow>
          <ConfirmButton
            confirmMessage="Eliminare la categoria?"
            icon={faTrash}
            text="Elimina categoria"
            kind="danger"
            onClick={deleteCategory}
          />
        </Column>
      </Row>
      <Row>
        <Column grow>
          <TextEditor
            label="Nome"
            initialValue={category.name}
            onConfirm={value => updateProperty('name', value)}
          />
        </Column>
        <Column grow>
          <RemoteSelectEditor
            url="locations"
            label="Postazione"
            value={category.location}
            id={l => l.uuid}
            text={l => l.name}
            onSelectOption={value => updateProperty('location', value.uuid)}
          />
        </Column>
        <Column grow>
          <ColorInput
            label="Colore"
            value={category.color}
            onConfirm={value => updateProperty('color', value)}
          />
        </Column>
      </Row>
      <Row>
        <Column grow>
          <RemoteSelectEditor
            url="additions?generic=false"
            rows={9}
            cols={4}
            label="Varianti"
            value={category.additions}
            id={a => a.uuid}
            text={a => a.name}
            maxChars={180}
            onSelectOption={onSelectAddition}
            multiSelect
          />
        </Column>
      </Row>
      <Row>
        <Column grow>
          <p className="h6">Piatti</p>
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <SelectInput
            options={category.dishes}
            rows={4}
            cols={3}
            id={d => d.uuid}
            text={d => d.name}
            bg={d => statusColor(d.status)}
            onSelectOption={dish => setSelectedDish(dish.uuid)}
            multiSelect
          />
        </Column>
        {selectedDish && (
          <DishEditor
            onClose={() => setSelectedDish(null)}
            dish={selectedDish}
            refresh={refresh}
            navigate={navigate}
          />
        )}
      </Row>
      <Row>
        <Column grow>
          <Button
            kind="success"
            text="Nuovo piatto"
            icon={faPlus}
            onClick={createDish}
          />
        </Column>
      </Row>
    </Column>
  );
}
