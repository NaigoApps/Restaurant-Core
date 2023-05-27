import React from 'react';
import { faTimes, faCheck } from '@fortawesome/free-solid-svg-icons';
import ButtonGroup from '../widgets/ButtonGroup';
import Button from '../widgets/Button';
import Row from '../widgets/Row';
import Column from '../widgets/Column';

export default function BooleanInput({
  label,
  value,
  onConfirm,
}) {
  return (
    <Row>
      <Column justifyCenter>
        {label}
      </Column>
      <Column>
        <ButtonGroup>
          <Button
            kind={!value ? 'danger' : 'secondary'}
            icon={faTimes}
            onClick={() => onConfirm(false)}
          />
          <Button
            kind={value ? 'success' : 'secondary'}
            icon={faCheck}
            onClick={() => onConfirm(true)}
          />
        </ButtonGroup>
      </Column>
    </Row>
  );
}
