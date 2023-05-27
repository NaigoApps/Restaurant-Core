import React, { Fragment } from 'react';
import Column from './Column';
import Row from './Row';
import Button from './Button';

const BUTTONS = [
  ['1', '2', '3'],
  ['4', '5', '6'],
  ['7', '8', '9'],
  ['0', ','],
];

export const CANC = 'CANC';

export default function ({
  disabled,
  onChar
}) {
  const n1 = BUTTONS[0].map(n => (
    <Column grow>
      <Button
        key={n}
        disabled={disabled}
        text={n}
        onClick={() => onChar(n)}
      />
    </Column>
  ));
  const n2 = BUTTONS[1].map(n => (
    <Column grow>
      <Button
        key={n}
        disabled={disabled}
        text={n}
        onClick={() => onChar(n)}
      />
    </Column>
  ));
  const n3 = BUTTONS[2].map(n => (
    <Column grow>
      <Button
        key={n}
        disabled={disabled}
        text={n}
        onClick={() => onChar(n)}
      />
    </Column>
  ));
  const n4 = BUTTONS[3].map(n => (
    <Column grow>
      <Button
        key={n}
        disabled={disabled}
        text={n}
        onClick={() => onChar(n)}
      />
    </Column>
  ));

  return (
    <Fragment>
      <Column spaced>
        <Row spaced>
          {n1}
        </Row>
        <Row spaced>
          {n2}
        </Row>
        <Row spaced>
          {n3}
        </Row>
        <Row spaced>
          {n4}
        </Row>
        <Row spaced>
          <Column grow>
            <Button
              disabled={disabled}
              text="C"
              onClick={() => onChar('CANC')}
            />
          </Column>
        </Row>
      </Column>
    </Fragment>
  );
}
