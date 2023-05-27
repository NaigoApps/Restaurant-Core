import React from 'react';
import { tableOrdersTotal } from '../../../utils/OrdinationUtils';
import useRemote from '../../../utils/useRemote';
import { formatDiningTable, formatPrice } from '../../../utils/Utils';
import Column from '../../../widgets/Column';
import Group from '../../../widgets/Group';
import Row from '../../../widgets/Row';
import Scrollable from '../../../widgets/Scrollable';
import Wrap from '../../../widgets/Wrap';

export default function DiningTableReview({ table }) {
  const [settings] = useRemote('settings');

  if (!table || !settings) {
    return null;
  }

  const coverChargesPrice = table.coverCharges * table.coverCharge;

  const total = tableOrdersTotal(table) + coverChargesPrice;

  return (
    <Row grow>
      <Column grow>
        <p className="h4 has-text-centered">{formatDiningTable(table)}</p>
        <Column grow>
          <Scrollable>
            <Row>
              <Column grow>
                <Row justifyBetween spaced>
                  <div>
                    {`${table.coverCharges} ${
                      table.coverCharges === 1 ? 'COPERTO' : 'COPERTI'
                    }`}
                  </div>
                  <div>
                    {formatPrice(coverChargesPrice)}
                  </div>
                </Row>
                {table.orders.map(group => (
                  <Group key={group.uuid} group={group}/>
                ))}
              </Column>
            </Row>
          </Scrollable>
        </Column>
        <Column>
          <Row justifyBetween>
            <span className="has-text-weight-bold">TOTALE: </span>
            <span>{formatPrice(total)}</span>
          </Row>
        </Column>
      </Column>
    </Row>
  )
    ;
}
