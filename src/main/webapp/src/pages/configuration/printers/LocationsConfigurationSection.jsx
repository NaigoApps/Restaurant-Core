import React, { Fragment, useState } from 'react';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import Row from '../../../widgets/Row';
import Column from '../../../widgets/Column';
import useRemote from '../../../utils/useRemote';
import SelectInput from '../../../inputs/SelectInput';
import useNetwork from '../../../utils/useNetwork';
import LocationEditor from './LocationEditor';
import Wrap from '../../../widgets/Wrap';
import Button from '../../../widgets/Button';

export default function LocationsConfigurationSection() {
  const { post } = useNetwork();

  const [locations, refresh] = useRemote('locations');

  const [selected, setSelected] = useState(null);

  async function createLocation() {
    const result = await post('locations');
    if (result != null) {
      refresh();
      setSelected(result);
    }
  }

  return (
    <Fragment>
      <Row>
        <Column grow>
          <p className="h4 has-text-centered">Postazioni</p>
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <SelectInput
            options={locations || []}
            id={location => location.uuid}
            text={location => location.name}
            rows={4}
            cols={2}
            onSelectOption={location => setSelected(location.uuid)}
          />
        </Column>
        {selected && (
          <LocationEditor
            onClose={() => setSelected(null)}
            location={selected}
            refresh={refresh}
          />
        )}
      </Row>
      <Row>
        <Column grow>
          <Button
            text="Nuova postazione"
            icon={faPlus}
            kind="success"
            onClick={createLocation}
          />
        </Column>
      </Row>
    </Fragment>
  );
}
