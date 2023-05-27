import React, { Fragment, useState } from 'react';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import Row from '../../../widgets/Row';
import Column from '../../../widgets/Column';
import useRemote from '../../../utils/useRemote';
import SelectInput from '../../../inputs/SelectInput';
import useNetwork from '../../../utils/useNetwork';
import PrinterEditor from './PrinterEditor';
import Button from '../../../widgets/Button';
import Wrap from '../../../widgets/Wrap';

export default function PrintersConfigurationSection() {
  const { post } = useNetwork();

  const [printers, refresh] = useRemote('printers');

  const [selected, setSelected] = useState(null);

  async function createPrinter() {
    const result = await post('printers');
    if (result != null) {
      refresh();
      setSelected(result);
    }
  }

  return (
    <Fragment>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">Stampanti</p>
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <SelectInput
            options={printers || []}
            id={printer => printer.uuid}
            text={printer => printer.name}
            rows={4}
            cols={2}
            onSelectOption={printer => setSelected(printer.uuid)}
          />
        </Column>
        {selected && (
          <PrinterEditor
            onClose={() => setSelected(null)}
            printer={selected}
            refresh={refresh}
          />
        )}
      </Row>
      <Row>
        <Column grow>
          <Button
            text="Nuova stampante"
            icon={faPlus}
            kind="success"
            onClick={createPrinter}
          />
        </Column>
      </Row>
    </Fragment>
  );
}
