import React, { Fragment } from 'react';
import Column from './Column';
import Row from './Row';
import Button, { SIZES } from './Button';

const BUTTONS = [
  ['1', '2', '3', '4', '5'],
  ['6', '7', '8', '9', '0'],
];

export const CANC = 'CANC';

export default function ({
  disabled,
  onChar
}) {
  const n1 = BUTTONS[0].map(n => (
    <Column key={n} grow>
      <Button
        disabled={disabled}
        text={n}
        onClick={() => onChar(n)}
      />
    </Column>
  ));
  const n2 = BUTTONS[1].map(n => (
    <Column key={n} grow>
      <Button
        disabled={disabled}
        text={n}
        onClick={() => onChar(n)}
      />
    </Column>
  ));


  return (
    <Column spaced>
      <Row spaced>
        {n1}
      </Row>
      <Row spaced>
        {n2}
      </Row>
      <Row spaced>
        <Column grow>
          <Button
            disabled={disabled}
            text="C"
            size={SIZES.XSMALL}
            onClick={() => onChar('CANC')}
          />
        </Column>
      </Row>
    </Column>
  );
}
