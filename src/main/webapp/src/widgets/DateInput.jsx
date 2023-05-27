import React from 'react';
import * as moment from 'moment';
import IntegerEditor from '../inputs/IntegerEditor';
import SelectEditor from '../inputs/SelectEditor';
import Column from './Column';
import Row from './Row';
import { getMonths } from '../utils/Utils';

export default function DateInput({
  label,
  date,
  onDateChange,
}) {
  const days = [];
  for (let i = 0; i < date.daysInMonth(); i++) {
    days.push(i + 1);
  }

  return (
    <Row spaced alignCenter justifyCenter>
      <Column grow grid>
        <p className="has-text-right">{label}</p>
      </Column>
      <Column grow grid>
        <IntegerEditor
          label="Anno"
          initialValue={date.year()}
          min={1999}
          max={2100}
          onConfirm={value => onDateChange(moment(date)
            .year(value))}
        />
      </Column>
      <Column grow grid>
        <SelectEditor
          label="Mese"
          rows={4}
          options={getMonths()
            .map((m, i) => i)}
          text={m => getMonths()[m]}
          value={date.month()}
          onSelectOption={value => onDateChange(moment(date)
            .month(value))}
        />
      </Column>
      <Column grow grid>
        <SelectEditor
          label="Giorno"
          rows={5}
          cols={7}
          options={days}
          value={date.date()}
          onSelectOption={value => onDateChange(moment(date)
            .date(value))}
        />
      </Column>
    </Row>
  );
}
