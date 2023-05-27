import $ from 'jquery';
import React, { Fragment, useEffect, useState } from 'react';
import { randomUuid } from '../utils/Utils';
import Column from '../widgets/Column';
import Row from '../widgets/Row';
import Keyboard, {
  BACKSPACE, DELETE, LEFT, RIGHT,
} from './Keyboard';

export default function ({
  value,
  placeholder,
  disabled,
  onChange,
  password,
}) {
  const [caret, setCaret] = useState(value ? value.length : 0);
  const [id] = useState(randomUuid());

  useEffect(() => {
    const textInput = $(`#${id}`);
    setTimeout(() => {
      textInput[0].setSelectionRange(caret, caret);
      textInput[0].focus();
    }, 100);
  }, [caret, id]);

  function onCharImpl(char) {
    const oldText = value || '';
    let newText = oldText;
    switch (char.toUpperCase()) {
      case BACKSPACE:
        if (oldText.length > 0 && caret > 0) {
          newText = oldText.slice(0, caret - 1) + oldText.slice(caret, oldText.length);
          setCaret(c => c - 1);
        }
        break;
      case LEFT:
        if (caret > 0) {
          setCaret(c => c - 1);
        }
        break;
      case RIGHT:
        if (caret < oldText.length) {
          setCaret(c => c + 1);
        }
        break;
      default:
        if (char.length === 1) {
          newText = oldText.slice(0, caret) + char + oldText.slice(caret, oldText.length);
          setCaret(c => c + 1);
        }
        break;
    }

    onChange(newText);
  }

  function onKeyDown(data) {
    if (data.key === 'ArrowLeft') {
      onCharImpl(LEFT);
    } else if (data.key === 'ArrowRight') {
      onCharImpl(RIGHT);
    }
  }

  function onChar(char, evt) {
    if (char && !evt) {
      onCharImpl(char);
    } else {
      switch (evt.inputType) {
        case 'deleteContentBackward':
          onCharImpl(BACKSPACE);
          break;
        case 'deleteContentForward':
          onCharImpl(DELETE);
          break;
        default:
          if (char) {
            onCharImpl(char);
          }
          break;
      }
    }
  }

  function updateCaret() {
    const input = $(`#${id}`)[0];
    setCaret(input.selectionStart);
  }

  return (
    <Column spaced>
      <Row>
        <Column grow>
          <div className="field">
            <div className="control">
              <input
                id={id}
                className="input is-primary"
                type={password ? 'password' : 'text'}
                placeholder={placeholder}
                disabled={disabled}
                value={value || ''}
                onMouseUp={updateCaret}
                onKeyDown={data => onKeyDown(data)}
                onChange={data => onChar(data.nativeEvent.data, data.nativeEvent)}
              />
            </div>
          </div>
        </Column>
      </Row>
      <Row>
        <Column>
          <Keyboard disabled={disabled} onChar={char => onChar(char)}/>
        </Column>
      </Row>
    </Column>
  );
}
