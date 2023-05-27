import { faPlus } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import SelectInput from '../../../inputs/SelectInput';
import useNetwork from '../../../utils/useNetwork';
import useRemote from '../../../utils/useRemote';
import Column from '../../../widgets/Column';
import Row from '../../../widgets/Row';
import CustomerEditor from './CustomerEditor';
import Button from '../../../widgets/Button';

export default function CustomersConfigurationPage() {
  const { post } = useNetwork();

  const [customers, refresh] = useRemote('customers');

  const [selected, setSelected] = useState(null);

  async function createCustomer() {
    const result = await post('customers');
    if (result != null) {
      refresh();
      setSelected(result);
    }
  }

  return (
    <Fragment>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">Clienti</p>
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <SelectInput
            options={customers || []}
            id={customer => customer.uuid}
            text={customer => (`${customer.name} ${customer.surname}`)}
            rows={5}
            cols={5}
            onSelectOption={customer => setSelected(customer.uuid)}
          />
        </Column>
        {selected && (
          <CustomerEditor
            onClose={() => setSelected(null)}
            customer={selected}
            refresh={refresh}
          />
        )}
      </Row>
      <Button
        text="Nuovo cliente"
        icon={faPlus}
        kind="success"
        onClick={createCustomer}
      />
    </Fragment>
  );
}
