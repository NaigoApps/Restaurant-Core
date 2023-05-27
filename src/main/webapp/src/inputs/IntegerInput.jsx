import React, { Fragment, useState } from 'react';
import Column from '../widgets/Column';
import IntKeyPad, { CANC } from '../widgets/IntKeyPad';
import Row from '../widgets/Row';

export default function ({
  value, placeholder, disabled, onChange, hit,
}) {
  const [text, setText] = useState(value ? value.toString() : '0');

  function updateValue(txt) {
    const newValue = parseInt(txt);
    setText(txt);
    if (!isNaN(newValue) && newValue !== value) {
      onChange(newValue);
    }
  }

  function onChar(char) {
    let newText = text;
    if (char === CANC) {
      newText = '0';
    } else {
      if (newText === '0' || !hit) {
        newText = '';
      }
      newText += char;
    }
    updateValue(newText);
  }

  return (
    <Column spaced grow>
      <Row>
        <Column grow>
          <div className="field">
            <div className="control">
              <input
                className="input is-primary"
                type="text"
                placeholder={placeholder}
                disabled={disabled}
                value={text !== undefined ? text : ''}
                onChange={evt => updateValue(evt.target.value)}
              />
            </div>
          </div>
        </Column>
      </Row>
      <Row>
        <Column grow>
          <IntKeyPad disabled={disabled} onChar={char => onChar(char)} />
        </Column>
      </Row>
    </Column>
  );
}
