import React, { useState } from 'react';
import IntegerEditor from '../inputs/IntegerEditor';
import SelectEditor from '../inputs/SelectEditor';
import { distribute, isToday } from '../utils/Utils';
import Button from './Button';
import Column from './Column';
import Row from './Row';

const MONTHS = [
  'Gennaio',
  'Febbraio',
  'Marzo',
  'Aprile',
  'Maggio',
  'Giugno',
  'Luglio',
  'Agosto',
  'Settembre',
  'Ottobre',
  'Novembre',
  'Dicembre',
];

export default function ({
  initialDate,
  onChange
}) {
  const [month, setMonth] = useState(initialDate.getMonth());
  const [year, setYear] = useState(initialDate.getFullYear());

  let current = new Date(year, month, 1);

  while (current.getDay() !== 1) {
    current.setDate(current.getDate() - 1);
  }

  const dates = [];

  while (current.getMonth() !== month) {
    dates.push(current);
    current = new Date(current.getFullYear(), current.getMonth(), current.getDate() + 1);
  }

  while (current.getMonth() === month) {
    dates.push(current);
    current = new Date(current.getFullYear(), current.getMonth(), current.getDate() + 1);
  }

  while (current.getDay() !== 1) {
    dates.push(current);
    current = new Date(current.getFullYear(), current.getMonth(), current.getDate() + 1);
  }

  const rows = distribute(dates, 7);

  return (
    <Row>
      <Column grow spaced>
        <Column grow>
          <SelectEditor
            label="Mese"
            rows={4}
            options={MONTHS.map((m, i) => i)}
            text={m => MONTHS[m]}
            value={month}
            onSelectOption={setMonth}
          />
        </Column>
        <Column grow>
          <IntegerEditor
            label="Anno"
            initialValue={year}
            min={1999}
            max={2100}
            onConfirm={setYear}
          />
        </Column>
        <Column grow spaced>
          {rows.map(row => (
            <Row spaced>
              {row.map(d => (
                <Column key={d.getTime()} grow grid>
                  <Button
                    active={isToday(d)}
                    text={`${d.getDate()}/${d.getMonth() + 1}`}
                    onClick={() => onChange(d)}
                    kind={d.getMonth() === month && d.getFullYear() === year ? 'info' : 'white'}
                  />
                </Column>
              ))}
            </Row>
          ))}
        </Column>
      </Column>
    </Row>
  );
}
