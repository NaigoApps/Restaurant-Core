export function beautifyDate(strDate) {
  if (strDate) {
    return `${strDate.substr(8, 2)}/${strDate.substr(5, 2)}/${strDate.substr(0, 4)}`;
  }
  return '';
}

export function beautifyTime(strDate) {
  if (strDate) {
    return strDate.substr(strDate.indexOf('T') + 1, 5);
  }
  return '';
}

export function stringEquals(s1, s2) {
  if ((s1 && !s2) || (s2 && !s1)) {
    return false;
  }
  if (!s1 && !s2) {
    return true;
  }
  return s1.localeCompare(s2) === 0;
}

export function numberCompare(n1, n2) {
  if (n1 < n2) return -1;
  if (n1 > n2) return +1;
  return 0;
}

export function findByUuid(array, uuid) {
  return array.find(e => e.get('uuid') === uuid);
}

export function findIndexByUuid(array, uuid) {
  return array.findIndex(e => e.get('uuid') === uuid);
}

export function collectionsEquals(l1, l2) {
  if (l1 === l2 || (l1 && !l2) || (l2 && !l1)) {
    return false;
  }
  if (l1.length !== l2.length) {
    return false;
  }
  let ok = true;
  l1.forEach((element) => {
    ok = ok && l2.includes(element);
  });
  l2.forEach((element) => {
    ok = ok && l1.includes(element);
  });
  return ok;
}

export function distribute(array, value) {
  const result = [];
  let partition = [];
  if (array) {
    array.forEach((element) => {
      if (partition.length >= value) {
        result.push(partition);
        partition = [];
      }
      partition.push(element);
    });
    if (partition.length > 0) {
      result.push(partition);
    }
  }
  return result;
}

export function randomUuid() {
  return `UI_${Math.random()
    .toString(16)
    .slice(2)}`;
}

export function isToday(date) {
  const now = new Date();
  return (
    date.getDate() === now.getDate()
    && date.getMonth() === now.getMonth()
    && date.getFullYear() === now.getFullYear()
  );
}

export function isThisMonth(date) {
  const now = new Date();
  return date.getMonth() === now.getMonth() && date.getFullYear() === now.getFullYear();
}

export function formatPrice(price) {
  return `€${price.toFixed(2)}`;
}

export function formatGroup(group, options = {
  withName: true,
  withQuantity: true,
}) {
  const components = [];
  if (options.withQuantity) {
    components.push(group.quantity);
  }
  if (options.withName && group.dish) {
    components.push(group.dish.name);
  }
  components.push(...group.additions.map(a => a.name));
  if (group.notes) {
    components.push(group.notes);
  }
  return components.join(' ');
}

export function formatDiningTable(dt) {
  if (!dt) {
    return '?';
  }
  const table = dt.table ? dt.table.name : '?';
  const waiter = dt.waiter ? dt.waiter.name : '?';
  return `${table} ${waiter} (${beautifyTime(dt.openingTime)})`;
}

export function formatDiningTableBg(dt) {
  if (dt.status === 'OPEN') {
    return 'danger';
  }
  if (dt.status === 'CLOSING') {
    return 'warning';
  }
  return 'secondary';
}

export function getMonths() {
  return [
    'Gennaio', 'Febbraio', 'Marzo',
    'Aprile', 'Maggio', 'Giugno',
    'Luglio', 'Agosto', 'Settembre',
    'Ottobre', 'Novembre', 'Dicembre',
  ];
}
