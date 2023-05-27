import React, { useState } from 'react';
import { faLock, faAngleLeft, faAngleRight } from '@fortawesome/free-solid-svg-icons';
import Row from '../widgets/Row';
import Column from '../widgets/Column';
import Button, { SIZES } from '../widgets/Button';

const NUMBERS = ['\\', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '\'', 'ì'];

const UPPERCASE = [
  ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'é', '*'],
  ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'ç', '°', '§'],
  ['>', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', ';', ':', '_'],
];
const LOWERCASE = [
  ['q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'è', '+'],
  ['a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'ò', 'à', 'ù'],
  ['<', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '-'],
];

export const BACKSPACE = 'BACKSPACE';
export const LEFT = 'ARROWLEFT';
export const RIGHT = 'ARROWRIGHT';
export const SHIFT = 'SHIFT';
export const CTRL = 'CONTROL';
export const ALT = 'ALT';
export const META = 'META';
export const LOCK = 'CAPSLOCK';
export const DELETE = 'DELETE';

export default function ({ onChar }) {
  const [uppercase, setUppercase] = useState(false);

  function onButtonClick(char) {
    onChar(char);
  }

  function onDelClick() {
    onChar(BACKSPACE);
  }

  function onRightClick() {
    onChar(RIGHT);
  }

  function onLeftClick() {
    onChar(LEFT);
  }

  function onCapsLockClick() {
    setUppercase(upper => !upper);
  }

  const letters = uppercase ? UPPERCASE : LOWERCASE;

  const numbers = NUMBERS.map(n => (
    <Button
      size={SIZES.XXSMALL}
      //   reduced={this.props.reduced}
      key={n}
      text={n}
      onClick={() => onButtonClick(n)}
    />
  ));
  const letters1 = letters[0].map(n => (
    <Button
      size={SIZES.XXSMALL}
      // reduced={this.props.reduced}
      key={n}
      text={n}
      onClick={() => onButtonClick(n)}
    />
  ));
  const letters2 = letters[1].map(n => (
    <Button
      size={SIZES.XXSMALL}
      // reduced={this.props.reduced}
      key={n}
      text={n}
      onClick={() => onButtonClick(n)}
    />
  ));
  const letters3 = letters[2].map(n => (
    <Button
      size={SIZES.XXSMALL}
      // reduced={this.props.reduced}
      key={n}
      text={n}
      onClick={() => onButtonClick(n)}
    />
  ));

  return (
    <Row>
      <Column spaced>
        <Row spaced>
          {numbers}
          <Button text="canc" size={SIZES.XXSMALL} onClick={onDelClick}/>
        </Row>
        <Row spaced>{letters1}</Row>
        <Row spaced>
          <Button icon={faLock} size={SIZES.XXSMALL} onClick={onCapsLockClick}/>
          {letters2}
        </Row>
        <Row spaced>{letters3}</Row>
        <Row spaced>
          <Button icon={faAngleLeft} size={SIZES.XXSMALL} type="info" onClick={onLeftClick}/>
          <Button text="&nbsp;" size={SIZES.MEDIUM} onClick={() => onButtonClick(' ')}/>
          <Button icon={faAngleRight} size={SIZES.XXSMALL} type="info" onClick={onRightClick}/>
        </Row>
      </Column>
    </Row>
  );
}
