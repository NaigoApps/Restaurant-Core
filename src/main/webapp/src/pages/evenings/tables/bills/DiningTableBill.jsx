import {
  faBars,
  faDollarSign,
  faPrint,
  faTimes,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';
import numeral from 'numeral';
import React, { useState } from 'react';
import useNetwork from '../../../../utils/useNetwork';
import { formatPrice } from '../../../../utils/Utils';
import Alert from '../../../../widgets/Alert';
import Button from '../../../../widgets/Button';
import Column from '../../../../widgets/Column';
import Group from '../../../../widgets/Group';
import OptionsButton from '../../../../widgets/OptionsButton';
import Row from '../../../../widgets/Row';
import YesNoButton from '../../../../widgets/YesNoButton';
import RemoteSelectEditor from '../../../../inputs/RemoteSelectEditor';

function formatBill(bill) {
  const prog = numeral(bill.progressive)
    .format('000');
  if (!bill.printTime) {
    return `Conto ${prog}`;
  }
  if (!bill.customer) {
    return `Ricevuta ${prog}`;
  }
  return `Fattura ${prog}`;
}

export default function DiningTableBill({
  table,
  bill,
  refresh,
}) {
  const [menuBill, setMenuBill] = useState(null);

  const {
    post,
    put,
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

  async function printBill(generic) {
    const result = await post(`bills/${bill.uuid}/print?generic=${!!generic}`);
    if (result !== null) {
      refresh();
      setMenuBill(null);
    }
  }

  async function deleteBill() {
    const result = await remove(`bills/${bill.uuid}`);
    if (result !== null) {
      refresh();
      setMenuBill(null);
    }
  }

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
                <Row>
                  <Column grow>
                    <RemoteSelectEditor
                      url="customers"
                      label="Cliente"
                      value={menuBill && menuBill.customer}
                      id={c => c.uuid}
                      text={c => `${c.name} ${c.surname}`}
                      onSelectOption={async (value) => {
                        const updatedBill = await put(
                          `bills/${bill.uuid}/customer/${value.uuid}`,
                        );
                        refresh();
                        setMenuBill(updatedBill);
                      }}
                    />
                  </Column>
                  <Column>
                    <Button
                      kind="danger"
                      icon={faTimes}
                      onClick={async () => {
                        const updatedBill = await remove(
                          `bills/${bill.uuid}/customer`,
                        );
                        refresh();
                        setMenuBill(updatedBill);
                      }}
                    />
                  </Column>
                </Row>
                <YesNoButton
                  icon={faDollarSign}
                  text="Ricevuta/Fattura fisc."
                  message="Stampare conto generico?"
                  onYes={() => printBill(true)}
                  onNo={() => printBill(false)}
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
