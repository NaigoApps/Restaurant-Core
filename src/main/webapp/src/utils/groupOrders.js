function equals(a, b) {
  return (!a && !b) || a === b;
}

function orderEquals(o1, o2) {
  return equals(o1.dish.uuid, o2.dish.uuid)
    && equals(o1.price, o2.price)
    && equals(o1.notes === o2.notes)
    && o1.additions.map(a => a.uuid).every(uuid => o2.additions.map(a1 => a1.uuid).includes(uuid))
    && o2.additions.map(a => a.uuid).every(uuid => o1.additions.map(a1 => a1.uuid).includes(uuid));
}

export default function groupOrders(orders) {
  const result = [];
  orders.forEach((order) => {
    const target = result.find(o => orderEquals(o, order));
    if (target) {
      target.quantity += 1;
    } else {
      result.push({
        ...order,
        quantity: 1,
      });
    }
  });
  return result;
}
