import React, { Fragment, useState } from 'react';
import Button from '../widgets/Button';
import Modal from '../widgets/Modal';
import SelectInput from './SelectInput';
import Row from '../widgets/Row';
import Column from '../widgets/Column';

export default function SelectEditor({
  id = v => v,
  text = v => v,
  maxChars,
  options,
  value,
  disabled,
  kind,
  label,
  multiSelect,
  onSelectOption,
  valueId,
  ...others
}) {
  const [shown, setShown] = useState(false);

  function findOption(val) {
    return options.find(opt => id(opt) === val);
  }

  function onSelectOptionImpl(v) {
    if (!multiSelect) {
      setShown(false);
    }
    onSelectOption(v);
  }

  let valueObject;
  let val;
  if (!multiSelect) {
    if (valueId !== null && valueId !== undefined) {
      valueObject = findOption(value);
    } else {
      valueObject = value;
    }
    val = valueObject !== null && valueObject !== undefined ? text(valueObject) : '';
  } else {
    val = value.map(v => text(v))
      .join(', ');
    if (maxChars) {
      const fullSize = val.length;
      const limit = Math.min(fullSize, maxChars);
      val = val.substring(0, limit);
      if (val.length < fullSize) {
        val += '...';
      }
    }
  }

  return (
    <Fragment>
      <Modal visible={shown} onClose={() => setShown(!shown)}>
        <Column spaced>
          <Row grow>
            <Column grow>
              <SelectInput
                id={id}
                text={text}
                multiSelect={multiSelect}
                options={options}
                value={value}
                onSelectOption={onSelectOptionImpl}
                {...others}
              />
            </Column>
          </Row>
          <Row>
            <Column grow>
              <Button text="Chiudi" kind="info" onClick={() => setShown(false)} />
            </Column>
          </Row>
        </Column>
      </Modal>
      <Button
        disabled={disabled}
        kind={kind}
        text={`${label}: ${val}`}
        onClick={() => setShown(!shown)}
      />
    </Fragment>
  );
}
