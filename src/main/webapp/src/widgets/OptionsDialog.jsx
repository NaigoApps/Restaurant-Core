import React from 'react';
import Button from './Button';
import Column from './Column';
import Modal from './Modal';
import Row from './Row';


export default function OptionsDialog({
  children,
  options,
  onSelectOption,
  onCancel,
  size,
  visible,
}) {
  return (
    <Modal visible={visible} onClose={onCancel} size={size}>
      <Row grow>
        <Column>{children}</Column>
      </Row>
      <Row spaced>
        {options.map(opt => (
          <Column key={opt.label} auto>
            <Button
              kind={opt.kind}
              text={opt.label}
              icon={opt.icon}
              onClick={() => onSelectOption(opt)}
            />
          </Column>
        ))}
      </Row>
    </Modal>
  );
}
