import React, { useState } from 'react';
import { clone } from '../../../../utils/OrdinationUtils';
import useNetwork from '../../../../utils/useNetwork';
import Column from '../../../../widgets/Column';
import OkCancelDialog from '../../../../widgets/OkCancelDialog';
import Row from '../../../../widgets/Row';
import AdvancedInsertingSection from './AdvancedInsertingSection';
import InsertingSection from './InsertingSection';
import OrdinationReview from './OrdinationReview';

export default function OrdinationEditor({
  tableUuid, onCancel, onConfirm, initialOrdination = { orders: [] },
}) {
  const [ordination, setOrdination] = useState(initialOrdination);
  const [selectedDish, setSelectedDish] = useState(null);

  const [insertingCategory, setInsertingCategory] = useState(null);
  const [insertingPhase, setInsertingPhase] = useState(null);

  function match(g1, g2) {
    return (
      g1.dish.uuid === g2.dish.uuid
      && g1.price === g2.price
      && g1.additions.every(addition => g2.additions.find(a => a.uuid === addition.uuid) != null)
      && g2.additions.every(addition => g1.additions.find(a => a.uuid === addition.uuid) != null)
      && ((!g2.notes && !g1.notes) || g2.notes === g1.notes)
    );
  }

  const { put, post } = useNetwork();

  async function onOk() {
    if (ordination.uuid) {
      await put(`dining-tables/${tableUuid}/ordinations/${ordination.uuid}`, ordination);
    } else {
      await post(`dining-tables/${tableUuid}/ordinations`, ordination);
    }
    onConfirm();
  }

  function doAddGroup(targetOrdination, phase, group) {
    let p = targetOrdination.orders.find(grp => grp.phase.uuid === phase.uuid);
    if (!p) {
      p = {
        phase,
        orders: [],
      };
      targetOrdination.orders.push(p);
    }
    const pI = targetOrdination.orders.indexOf(p);
    let o = p.orders.find(grp => match(grp, group));
    if (!o) {
      o = group;
      p.orders.push(o);
    } else {
      o.quantity += group.quantity;
    }
    const gI = p.orders.indexOf(o);

    targetOrdination.orders.sort((o1, o2) => o1.phase.priority - o2.phase.priority);

    return [pI, gI];
  }

  function addDish(dish, quantity, price, additions, notes) {
    setOrdination((oldOrdination) => {
      const newOrdination = clone(oldOrdination);
      doAddGroup(newOrdination, insertingPhase, {
        dish,
        price,
        quantity,
        additions,
        notes,
      });
      return newOrdination;
    });
    setSelectedDish(null);
  }

  function onLess(phaseIndex, groupIndex) {
    setOrdination((oldOrdination) => {
      const newOrdination = clone(oldOrdination);
      const phases = newOrdination.orders;
      const groups = phases[phaseIndex].orders;
      const group = groups[groupIndex];
      group.quantity -= 1;
      if (group.quantity === 0) {
        groups.splice(groupIndex, 1);
        if (groups.length === 0) {
          phases.splice(phaseIndex, 1);
        }
      }
      return newOrdination;
    });
  }

  function onMore(phaseIndex, groupIndex) {
    setOrdination((oldOrdination) => {
      const newOrdination = clone(oldOrdination);
      const phases = newOrdination.orders;
      const groups = phases[phaseIndex].orders;
      const group = groups[groupIndex];
      group.quantity += 1;
      return newOrdination;
    });
  }

  return (
    <OkCancelDialog
      size="lg"
      visible
      onCancel={onCancel}
      onOk={onOk}
    >
      <Row grow>
        <Column grow>
          <Row grow>
            <Column grow>
              <OrdinationReview ordination={ordination} />
            </Column>
          </Row>
        </Column>
        <Column grow>
          <InsertingSection
            ordination={ordination}
            category={insertingCategory}
            onSelectCategory={setInsertingCategory}
            phase={insertingPhase}
            onSelectPhase={setInsertingPhase}
            onSelectDish={dish => addDish(dish, 1, dish.price, [], null)}
            onCustomize={setSelectedDish}
            onLess={onLess}
            onMore={onMore}
          />
        </Column>
      </Row>
      {selectedDish && (
        <AdvancedInsertingSection
          dish={selectedDish}
          onCancel={() => setSelectedDish(null)}
          onOk={(qty, price, adds, notes) => addDish(selectedDish, qty, price, adds, notes)}
        />
      )}
    </OkCancelDialog>
  );
}
