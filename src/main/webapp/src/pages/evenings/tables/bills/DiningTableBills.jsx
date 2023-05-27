import React from 'react';
import { formatDiningTable } from '../../../../utils/Utils';
import Column from '../../../../widgets/Column';
import Row from '../../../../widgets/Row';
import Scrollable from '../../../../widgets/Scrollable';
import DiningTableBill from './DiningTableBill';

export default function DiningTableBills({
  table,
  refresh,
}) {
  if (!table) {
    return null;
  }

  return (
    <Row grow>
      <Column grow>
        <Row>
          <Column grow>
            <p className="h4 has-text-centered">{formatDiningTable(table)}</p>
          </Column>
        </Row>
        <Row grow>
          <Column grow>
            <Scrollable>
              {table.bills.map(bill => (
                <Row key={bill.uuid} bitSpaced>
                  <Column grow>
                    <DiningTableBill
                      table={table}
                      bill={bill}
                      refresh={refresh}
                    />
                  </Column>
                </Row>
              ))}
            </Scrollable>
          </Column>
        </Row>
      </Column>
    </Row>
  );
}
