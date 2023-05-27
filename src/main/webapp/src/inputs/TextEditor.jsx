import { faCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import Button from '../widgets/Button';
import Column from '../widgets/Column';
import Modal from '../widgets/Modal';
import Row from '../widgets/Row';
import TextInput from './TextInput';

export default function ({
  label,
  disabled,
  kind = 'secondary',
  initialValue,
  onConfirm,
  password,
  ...others
}) {
  const [value, setValue] = useState(initialValue);
  const [shown, setShown] = useState(false);

  function show() {
    setValue(initialValue);
    setShown(true);
  }

  function hide() {
    setShown(false);
  }

  function updateValue(v) {
    setValue(v);
  }

  const text = `${label}: ${initialValue && !password ? initialValue.toString() : ''}`;

  return (
    <Fragment>
      <Modal visible={shown} onClose={hide} lg>
        <Column spaced>
          <Row>
            <Column grow>
              <TextInput
                value={value}
                onChange={updateValue}
                password={password}
                {...others}
              />
            </Column>
          </Row>
          <Row spaced>
            <Column grow>
              <Button
                kind="danger"
                icon={faTimes}
                onClick={hide}
              />
            </Column>
            <Column grow>
              <Button
                kind="success"
                icon={faCheck}
                onClick={() => {
                  hide();
                  setShown(false);
                  onConfirm(value);
                }}
              />
            </Column>
          </Row>
        </Column>
      </Modal>
      <Button
        disabled={disabled}
        kind={kind}
        highPadding
        text={text}
        onClick={show}
      />
    </Fragment>
  );
}
