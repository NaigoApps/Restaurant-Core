import { faCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import Button from '../widgets/Button';
import Column from '../widgets/Column';
import Modal from '../widgets/Modal';
import Row from '../widgets/Row';
import IntegerInput from './IntegerInput';

export default function ({
  label,
  initialValue,
  disabled,
  kind,
  onConfirm,
  ...others
}) {
  const [value, setValue] = useState(initialValue);
  const [shown, setShown] = useState(false);
  const [hit, setHit] = useState(false);

  function show() {
    setHit(false);
    setValue(initialValue);
    setShown(true);
  }

  function hide() {
    setShown(false);
  }

  function updateValue(v) {
    setHit(true);
    setValue(v);
  }

  const text = `${label}: ${initialValue ? initialValue.toString() : ''}`;
  return (
    <Fragment>
      <Modal visible={shown} onClose={hide}>
        <Column spaced>
          <Row>
            <Column grow>
              <IntegerInput
                value={value}
                onChange={updateValue}
                hit={hit}
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
