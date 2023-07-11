import {faPlus} from '@fortawesome/free-solid-svg-icons';
import React, {useState} from 'react';
import FloatEditor from '../../inputs/FloatEditor';
import SelectInput from '../../inputs/SelectInput';
import useNetwork from '../../utils/useNetwork';
import useRemote from '../../utils/useRemote';
import {formatDiningTable, formatDiningTableBg} from '../../utils/Utils';
import Button from '../../widgets/Button';
import Column from '../../widgets/Column';
import Row from '../../widgets/Row';
import DiningTableDataEditor from './tables/DiningTableDataEditor';
import SockJsClient from "react-stomp";

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

  if (!evening) {
    return null;
  }

  const diningTables = evening.diningTables.filter(table => table.status !== 'ARCHIVED');

  return (
      <>
        <SockJsClient
            url={'http://localhost:8080/restaurant'}
            topics={['/topic/dining-tables']}
            onConnect={() => console.log("Connected to dining tables ws")}
            onDisconnect={() => console.log("Disconnected from dining tables ws")}
            onMessage={() => {
              console.log("New dining table detected")
              refresh();
            }}
        />
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
      </>
  );
}
