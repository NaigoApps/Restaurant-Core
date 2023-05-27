import { faCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import Button from './Button';
import Column from './Column';
import Row from './Row';

export default function Editor({
  initialData,
  onOk,
  onCancel,
  children,
}) {
  return (
    <Row grow>
      <Column grow spaced>
        <Row grow>
          <Column grow>{children}</Column>
        </Row>
        <Row spaced>
          <Column grow>
            <Button kind="danger" icon={faTimes} onClick={onCancel}/>
          </Column>
          <Column grow>
            <Button
              kind="success"
              icon={faCheck}
              onClick={() => onOk(initialData)}
            />
          </Column>
        </Row>
      </Column>
    </Row>
  );
}
