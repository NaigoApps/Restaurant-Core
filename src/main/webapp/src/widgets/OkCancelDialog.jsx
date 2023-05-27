import React from 'react';

import { faTimes, faCheck } from '@fortawesome/free-solid-svg-icons';
import Row from './Row';
import Column from './Column';
import Button from './Button';
import Modal from './Modal';

export default function OkCancelDialog({
  children,
  size,
  visible,
  onCancel,
  onOk,
  okText,
  cancelText,
}) {
  return (
    <Modal visible={visible} onClose={onCancel} size={size}>
      <Column grow spaced>
        <Row grow>
          <Column grow>{children}</Column>
        </Row>
        <Row spaced>
          <Column grow>
            <Button
              kind="danger"
              text={cancelText || 'Annulla'}
              icon={faTimes}
              onClick={onCancel}
            />
          </Column>
          <Column grow>
            <Button
              kind="success"
              text={okText || 'Conferma'}
              icon={faCheck}
              onClick={onOk}
            />
          </Column>
        </Row>
      </Column>
    </Modal>
  );
}
