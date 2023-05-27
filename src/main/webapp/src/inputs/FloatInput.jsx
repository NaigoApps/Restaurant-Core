import React, { useState } from 'react';
import numeral from 'numeral';
import Column from '../widgets/Column';
import { CANC } from '../widgets/IntKeyPad';
import KeyPad from '../widgets/KeyPad';
import Row from '../widgets/Row';

export default function FloatInput({
  value, placeholder, disabled, onChange, hit, currency,
}) {
  const [text, setText] = useState(value.toString());

  function updateValue(txt) {
    let wrapped = numeral(txt);
    setText(txt);
    if (!isNaN(wrapped.value())) {
      if (currency) {
        wrapped = numeral(wrapped.format('0.00'));
      }
      const newValue = wrapped.value();
      if (newValue !== value) {
        onChange(newValue);
      }
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
    <Column spaced>
      <Row>
        <Column grow>
          <div className="field">
            <div className="control">
              <input
                className="input is-primary"
                type="text"
                placeholder={placeholder}
                disabled={disabled}
                value={text || ''}
                onChange={data => updateValue(data)}
              />
            </div>
          </div>
        </Column>
      </Row>
      <Row>
        <Column grow>
          <KeyPad disabled={disabled} onChar={char => onChar(char)} />
        </Column>
      </Row>
    </Column>
  );
}
