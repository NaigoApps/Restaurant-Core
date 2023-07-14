import {
  faBars, faPrint, faTimes, faTrash,
} from '@fortawesome/free-solid-svg-icons';
import numeral from 'numeral';
import React, { useCallback, useState } from 'react';
import useNetwork from '../../../../utils/useNetwork';
import { formatPrice } from '../../../../utils/Utils';
import Alert from '../../../../widgets/Alert';
import Button from '../../../../widgets/Button';
import Column from '../../../../widgets/Column';
import Group from '../../../../widgets/Group';
import OptionsButton from '../../../../widgets/OptionsButton';
import Row from '../../../../widgets/Row';
import YesNoButton from '../../../../widgets/YesNoButton';

function formatBill(bill) {
  const prog = numeral(bill.progressive)
    .format('000');
  if (!bill.printTime) {
    return `Conto ${prog}`;
  }
  return `Ricevuta ${prog}`;
}

export default function DiningTableBill({
  table,
  bill,
  refresh,
}) {
  const [menuBill, setMenuBill] = useState(null);

  const {
    post,
    remove,
  } = useNetwork();

  async function printPreBill(generic) {
    const result = await post(
      `bills/${bill.uuid}/soft-print?generic=${!!generic}`,
    );
    if (result !== null) {
      refresh();
      setMenuBill(null);
    }
  }

  const deleteBill = useCallback(async () => {
    const result = await remove(`bills/${bill.uuid}`);
    if (result !== null) {
      refresh();
      setMenuBill(null);
    }
  }, [bill.uuid, refresh, remove]);

  async function deleteBillAndOrders() {
    const result = await remove(`bills/${bill.uuid}/deep`);
    if (result !== null) {
      refresh();
      setMenuBill(null);
    }
  }

  if (!bill) {
    return null;
  }

  return (
    <div className="card">
      <header className="card-header">
        <p className="card-header-title h4">{formatBill(bill)}</p>
        <Row alignCenter>
          <Column>
            <Button
              icon={faBars}
              large
              onClick={() => setMenuBill(bill)}
            />
          </Column>
          {menuBill && (
            <Alert onClose={() => setMenuBill(null)} visible>
              <Column spaced grow>
                <YesNoButton
                  text="Stampa pre-conto"
                  icon={faPrint}
                  message="Stampare preconto generico?"
                  onYes={() => printPreBill(true)}
                  onNo={() => printPreBill(false)}
                />
                <OptionsButton
                  text="Elimina conto"
                  icon={faTrash}
                  kind="danger"
                  onClick={deleteBill}
                  options={[
                    {
                      label: 'Elimina conto',
                      kind: 'danger',
                      onClick: deleteBill,
                    }, {
                      label: 'Elimina conto e ordini',
                      kind: 'danger',
                      onClick: deleteBillAndOrders,
                    }, {
                      label: 'Annulla',
                      kind: 'secondary',
                      onClick: () => setMenuBill(null),
                    },
                  ]}
                  message="Eliminare il conto?"
                />
              </Column>
            </Alert>
          )}
        </Row>
      </header>
      <div className="card-content">
        <Row>
          <Column grow>
            <Row justifyBetween>
              <div>
                {`${bill.coverCharges} COPERTI`}
              </div>
              <div>
                {formatPrice(bill.coverCharges * table.coverCharge)}
              </div>
            </Row>
            {bill.orders.map(group => (
              <Group group={group} />
            ))}
            <Row justifyBetween>
              <div>
                <b>TOTALE</b>
              </div>
              <div>
                <b>{formatPrice(bill.total)}</b>
              </div>
            </Row>
          </Column>
        </Row>
      </div>
    </div>
  );
}
