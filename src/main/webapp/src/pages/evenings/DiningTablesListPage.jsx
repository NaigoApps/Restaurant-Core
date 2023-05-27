import { faPlus } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState, useEffect } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import FloatEditor from '../../inputs/FloatEditor';
import SelectInput from '../../inputs/SelectInput';
import useNetwork from '../../utils/useNetwork';
import useRemote from '../../utils/useRemote';
import { formatDiningTable, formatDiningTableBg } from '../../utils/Utils';
import Button from '../../widgets/Button';
import Column from '../../widgets/Column';
import Row from '../../widgets/Row';
import DiningTableDataEditor from './tables/DiningTableDataEditor';

export default function DiningTablesListPage({
  navigate,
  eveningDate,
}) {
  const [evening, refresh] = useRemote(`evenings?date=${eveningDate}`);

  const [editing, setEditing] = useState(false);

  const { put } = useNetwork();

  const updateCoverCharge = async (value) => {
    const result = await put(`evenings/${evening.uuid}/coverCharge`, value);
    if (result !== null) {
      refresh();
    }
  };

  useEffect(() => {
    const sock = new SockJS('/restaurant');
    const client = Stomp.over(sock);
    client.connect({}, () => client
      .subscribe('/topic/dining-tables', () => refresh()));
    return () => client.disconnect();
  }, [refresh]);

  if (!evening) {
    return null;
  }

  const diningTables = evening.diningTables.filter(table => table.status !== 'ARCHIVED');

  return (
    <Column grow spaced>
      <Row>
        <Column>
          <FloatEditor
            label="Coperto"
            initialValue={evening && evening.coverCharge}
            onConfirm={updateCoverCharge}
            currency
          />
        </Column>
      </Row>
      <Row grow>
        <Column grow>
          <Row>
            <Column grow>
              <p className="h3 has-text-centered">Elenco tavoli</p>
            </Column>
          </Row>
          <Row grow>
            <Column grow>
              <SelectInput
                rows={5}
                cols={4}
                options={diningTables}
                id={dt => dt.uuid}
                bg={formatDiningTableBg}
                text={formatDiningTable}
                onSelectOption={table => navigate(`${table.uuid}`)}
              />
            </Column>
          </Row>
        </Column>
        {editing && (
          <DiningTableDataEditor
            onClose={() => setEditing(false)}
            refresh={refresh}
          />
        )}
      </Row>
      <Row>
        <Column grow>
          <Button
            kind="success"
            icon={faPlus}
            text="Nuovo tavolo"
            onClick={() => setEditing(true)}
          />
        </Column>
      </Row>
    </Column>
  );
}
