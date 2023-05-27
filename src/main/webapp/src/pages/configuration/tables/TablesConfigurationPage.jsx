import { faPlus } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import SelectInput from '../../../inputs/SelectInput';
import useNetwork from '../../../utils/useNetwork';
import useRemote from '../../../utils/useRemote';
import Column from '../../../widgets/Column';
import Row from '../../../widgets/Row';
import TableEditor from './TableEditor';
import Wrap from '../../../widgets/Wrap';
import Button from '../../../widgets/Button';

export default function TablesConfigurationPage() {
  const { post } = useNetwork();

  const [tables, refresh] = useRemote('restaurant-tables');

  const [selected, setSelected] = useState(null);

  async function createTable() {
    const result = await post('restaurant-tables');
    if (result != null) {
      refresh();
      setSelected(result);
    }
  }

  return (
    <Fragment>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">Tavoli</p>
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <SelectInput
            options={tables || []}
            id={table => table.uuid}
            text={table => table.name}
            rows={5}
            cols={5}
            onSelectOption={table => setSelected(table.uuid)}
          />
        </Column>
        {selected && (
          <TableEditor
            onClose={() => setSelected(null)}
            table={selected}
            refresh={refresh}
          />
        )}
      </Row>
      <Button
        text="Nuovo tavolo"
        icon={faPlus}
        kind="success"
        onClick={createTable}
      />
    </Fragment>
  );
}
