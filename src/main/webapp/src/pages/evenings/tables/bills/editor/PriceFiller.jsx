import React, { useEffect } from 'react';
import FloatEditor from '../../../../../inputs/FloatEditor';
import useNetwork from '../../../../../utils/useNetwork';
import Column from '../../../../../widgets/Column';
import Row from '../../../../../widgets/Row';


export default function PriceFiller({ table, refresh, onDone }) {
  const { put } = useNetwork();

  async function updatePrice(order, price) {
    await put(`orders/${order.uuid}/price`, price);
    refresh();
  }

  useEffect(() => {
    const zeroOrders = !!table.orders.find(o => o.price === 0);
    if (!zeroOrders) {
      onDone();
    }
  }, [onDone, table.orders]);

  return (
    <Row>
      <Column>
        <Row>
          <Column>
            <p className="h4">Aggiorna il prezzo dei seguenti ordini</p>
          </Column>
        </Row>
        {table.orders
          .filter(o => o.price === 0)
          .map(o => (
            <Row>
              <Column>
                <FloatEditor
                  label={o.dish.name}
                  initialValue={o.price}
                  currency
                  onConfirm={value => updatePrice(o.orders[0], value)}
                />
              </Column>
            </Row>
          ))
        }
      </Column>
    </Row>
  );
}
