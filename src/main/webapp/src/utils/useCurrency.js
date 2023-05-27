export default function (value) {
  if (value !== null && value !== undefined) {
    return `€${value.toFixed(2)}`;
  }
  return '?';
}
