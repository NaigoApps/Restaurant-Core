
const cloneDeep = require('lodash.clonedeep');

export function dishOrders(ordination, dishUuid) {
  const result = [];
  ordination.orders.forEach((phaseGroup, pIndex) => {
    phaseGroup.orders.forEach((group, gIndex) => {
      if (group.dish.uuid === dishUuid) {
        result.push({
          phase: phaseGroup.phase,
          group,
          phaseIndex: pIndex,
          groupIndex: gIndex,
        });
      }
    });
  });
  return result;
}

export function tableOrdersTotal(table) {
  let total = 0;
  table.orders.forEach((group) => {
    total += group.quantity * group.price;
  });
  return total;
}

export function ordinationTotal(ordination) {
  let total = 0;
  ordination.orders.forEach((phaseGroup) => {
    phaseGroup.orders.forEach((group) => {
      total += group.quantity * group.price;
    });
  });
  return total;
}

export function clone(ordination) {
  return cloneDeep(ordination);
}
