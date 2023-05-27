import React, { Fragment, useState } from 'react';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import Row from '../../../widgets/Row';
import Column from '../../../widgets/Column';
import useRemote from '../../../utils/useRemote';
import SelectInput from '../../../inputs/SelectInput';
import useNetwork from '../../../utils/useNetwork';
import WaiterEditor from './WaiterEditor';
import Button from '../../../widgets/Button';
import Wrap from '../../../widgets/Wrap';

export default function WaitersConfigurationPage() {
  const { post } = useNetwork();

  const [waiters, refresh] = useRemote('waiters');

  const [selected, setSelected] = useState(null);

  async function createWaiter() {
    const result = await post('waiters');
    if (result != null) {
      refresh();
      setSelected(result);
    }
  }

  function statusColor(status) {
    if (status === 'SUSPENDED') {
      return 'warning';
    } if (status === 'REMOVED') {
      return 'danger';
    }
    return 'secondary';
  }

  return (
    <Fragment>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">Camerieri</p>
        </Column>
      </Row>
      <Row grow relative>
        <Column grow>
          <SelectInput
            options={waiters || []}
            id={w => w.uuid}
            text={w => w.name}
            bg={w => statusColor(w.status)}
            rows={4}
            cols={4}
            onSelectOption={w => setSelected(w.uuid)}
          />
        </Column>
        {selected && (
        <WaiterEditor
          onClose={() => setSelected(null)}
          waiter={selected}
          refresh={refresh}
        />
        )}
      </Row>
      <Wrap>
        <Button
          text="Nuovo cameriere"
          icon={faPlus}
          kind="success"
          onClick={createWaiter}
        />
      </Wrap>
    </Fragment>
  );
}
