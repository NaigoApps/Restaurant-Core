import React from 'react';
import Button from './Button';
import Column from './Column';
import Modal from './Modal';
import Row from './Row';


export default function Alert({
  children,
  size,
  visible,
  onClose,
}) {
  return (
    <Modal visible={visible} onClose={onClose} size={size}>
      <Column spaced grow>
        <Row>
          <Column grow>{children}</Column>
        </Row>
        <Row>
          <Column grow>
            <Button
              kind="secondary"
              text="Chiudi"
              onClick={onClose}
            />
          </Column>
        </Row>
      </Column>
    </Modal>
  );
}
