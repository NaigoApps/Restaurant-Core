import React, { Fragment, useState } from 'react';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import Row from '../../../widgets/Row';
import Column from '../../../widgets/Column';
import useRemote from '../../../utils/useRemote';
import SelectInput from '../../../inputs/SelectInput';
import useNetwork from '../../../utils/useNetwork';
import AdditionEditor from './AdditionEditor';
import Wrap from '../../../widgets/Wrap';
import Button from '../../../widgets/Button';

export default function AdditionsConfigurationPage() {
  const { post } = useNetwork();

  const [additions, refresh] = useRemote('additions');

  const [selected, setSelected] = useState(null);

  async function createAddition() {
    const result = await post('additions');
    if (result != null) {
      refresh();
      setSelected(result);
    }
  }

  return (
    <Fragment>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">Varianti</p>
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <SelectInput
            options={additions || []}
            id={addition => addition.uuid}
            text={addition => addition.name}
            rows={6}
            cols={4}
            onSelectOption={addition => setSelected(addition.uuid)}
          />
        </Column>
        {selected && (
          <AdditionEditor
            onClose={() => setSelected(null)}
            addition={selected}
            refresh={refresh}
          />
        )}
      </Row>
      <Row>
        <Column grow>
          <Button icon={faPlus} text="Nuova variante" kind="success" onClick={createAddition}/>
        </Column>
      </Row>
    </Fragment>
  );
}
