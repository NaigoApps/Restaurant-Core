import React, { useEffect, useState } from 'react';
import BillSplitPage from './BillSplitPage';
import BillReviewPage from './BillReviewPage';
import useNetwork from '../../../../../utils/useNetwork';
import OkCancelDialog from '../../../../../widgets/OkCancelDialog';

export default function BillEditor({
  table, onClose, refresh,
}) {
  const { get, post } = useNetwork();

  const [review, setReview] = useState(false);

  const [selectedCcs, setSelectedCcs] = useState(0);
  const [availableCcs, setAvailableCcs] = useState(0);
  const [availableOrders, setAvailableOrders] = useState([]);

  const [finalTotal, setFinalTotal] = useState(0);

  useEffect(() => {
    get(`bills/remaining/${table.uuid}`)
      .then((remainingTable) => {
        setAvailableCcs(remainingTable.coverCharges);
        setAvailableOrders(remainingTable.orders.map((group, index) => ({
          ...group,
          id: index,
          selected: 0,
        })));
      });
  }, [get, table.uuid]);

  async function onConfirm() {
    if (!review) {
      setReview(true);
    } else {
      await post(`bills?table=${table.uuid}`, {
        ccs: selectedCcs,
        orders: availableOrders
          .map(group => group.orders.slice(0, group.selected)
            .map(order => order.uuid))
          .reduce((a1, a2) => a1.concat(a2), []),
        total: finalTotal
      });
      await refresh();
      onClose();
    }
  }

  return (
    <OkCancelDialog
      cancelText="Annulla conto"
      onCancel={onClose}
      okText="Conferma conto"
      onOk={onConfirm}
      size="lg"
      visible
    >
      {!review && (
        <BillSplitPage
          table={table}
          selectedCcs={selectedCcs}
          availableCcs={availableCcs}
          availableOrders={availableOrders}
          setAvailableOrders={setAvailableOrders}
          setSelectedCcs={setSelectedCcs}
          setFinalTotal={setFinalTotal}
        />
      )}
      {review && (
        <BillReviewPage
          table={table}
          availableOrders={availableOrders}
          selectedCcs={selectedCcs}
          finalTotal={finalTotal}
          setFinalTotal={setFinalTotal}
          refresh={refresh}
        />
      )}
    </OkCancelDialog>
  );
}
