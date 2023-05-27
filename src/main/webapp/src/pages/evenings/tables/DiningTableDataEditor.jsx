import React from 'react';
import IntegerEditor from '../../../inputs/IntegerEditor';
import RemoteSelectEditor from '../../../inputs/RemoteSelectEditor';
import useEditor from '../../../utils/useEditor';
import Column from '../../../widgets/Column';
import Editor from '../../../widgets/Editor';
import Modal from '../../../widgets/Modal';
import Row from '../../../widgets/Row';

export default function DiningTableDataEditor({
  onClose,
  table: tableUuid,
  refresh: refreshAll,
}) {
  const [table, setTable, confirm] = useEditor({
    url: 'dining-tables',
    uuid: tableUuid,
    initialValue: {},
  });

  async function onOk() {
    await confirm();
    refreshAll();
    onClose();
  }

  if (!table) {
    return null;
  }

  return (
    <Modal onClose={onClose} visible>
      <Editor onCancel={onClose} onOk={onOk}>
        <Column grow spaced>
          <Row>
            <Column grow>
              <IntegerEditor
                label="Coperti"
                initialValue={table.coverCharges || 0}
                onConfirm={(value) => {
                  setTable({
                    ...table,
                    coverCharges: value
                  });
                }}
              />
            </Column>
          </Row>
          <Row>
            <Column grow>
              <RemoteSelectEditor
                url="waiters/active"
                label="Cameriere"
                id={w => w.uuid}
                text={w => w.name}
                value={table.waiter}
                onSelectOption={(value) => {
                  setTable({
                    ...table,
                    waiter: value
                  });
                }}
              />
            </Column>
          </Row>
          <Row>
            <Column grow>
              <RemoteSelectEditor
                url="restaurant-tables"
                label="Tavolo"
                rows={5}
                cols={5}
                id={t => t.uuid}
                text={t => t.name}
                bg={t => (t.busy ? 'danger' : 'secondary')}
                value={table.table}
                onSelectOption={(value) => {
                  setTable({
                    ...table,
                    table: value
                  });
                }}
              />
            </Column>
          </Row>
        </Column>
      </Editor>
    </Modal>
  );
}
