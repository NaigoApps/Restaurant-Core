import { faPlus } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment } from 'react';
import SelectInput from '../../../inputs/SelectInput';
import useNetwork from '../../../utils/useNetwork';
import useRemote from '../../../utils/useRemote';
import Column from '../../../widgets/Column';
import Row from '../../../widgets/Row';
import Wrap from '../../../widgets/Wrap';
import Button from '../../../widgets/Button';

export default function CategoriesConfigurationSection({ navigate }) {
  const { post } = useNetwork();

  const [categories, refresh] = useRemote('categories');

  async function createCategory() {
    const result = await post('categories');
    if (result != null) {
      await refresh();
      navigate(result);
    }
  }

  return (
    <Fragment>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">Categorie</p>
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <SelectInput
            options={categories || []}
            id={cat => cat.uuid}
            text={cat => cat.name}
            bg={cat => cat.color}
            rows={4}
            cols={4}
            onSelectOption={cat => navigate(cat.uuid)}
          />
        </Column>
      </Row>
      <Row>
        <Column grow>
          <Button
            icon={faPlus}
            text="Nuova categoria"
            kind="success"
            onClick={createCategory}
          />
        </Column>
      </Row>
    </Fragment>
  );
}
