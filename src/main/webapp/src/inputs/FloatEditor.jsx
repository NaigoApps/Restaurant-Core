import { faCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import useCurrency from '../utils/useCurrency';
import Button from '../widgets/Button';
import Column from '../widgets/Column';
import Modal from '../widgets/Modal';
import Row from '../widgets/Row';
import FloatInput from './FloatInput';

export default function FloatEditor({
  label,
  initialValue,
  currency,
  disabled,
  type,
  onConfirm,
  ...others
}) {
  const [value, setValue] = useState(initialValue);
  const [shown, setShown] = useState(false);
  const [hit, setHit] = useState(false);

  const formattedValue = useCurrency(initialValue);

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

  const text = `${label}: ${currency ? formattedValue : initialValue}`;
  return (
    <Fragment>
      <Modal visible={shown} onClose={hide}>
        <Column spaced>
          <Row>
            <Column grow>
              <FloatInput
                value={value}
                onChange={updateValue}
                hit={hit}
                {...others}
                currency={currency}
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
        type={type}
        highPadding
        text={text}
        onClick={show}
      />
    </Fragment>
  );
}
