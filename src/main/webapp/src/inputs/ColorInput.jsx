import React from 'react';
import SelectEditor from './SelectEditor';

export default function ColorInput({ label, onConfirm, value }) {
  return (
    <SelectEditor
      label={label}
      options={[
        'secondary', 'danger', 'info', 'success', 'warning', 'purple',
      ]}
      kind={value || 'secondary'}
      text={() => ''}
      bg={opt => opt}
      value={value}
      onSelectOption={onConfirm}
    />
  );
}
