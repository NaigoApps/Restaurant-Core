import React, { useCallback, useEffect, useState } from 'react';
import * as moment from 'moment';
import { Legend, Pie, PieChart } from 'recharts';
import useRemote from '../../utils/useRemote';
import Row from '../../widgets/Row';
import Column from '../../widgets/Column';
import DateInput from '../../widgets/DateInput';
import SelectEditor from '../../inputs/SelectEditor';
import Button from '../../widgets/Button';

const Stats = [
  {
    label: 'Categorie più vendute',
    resource: 'categories-profit',
  },
  {
    label: 'Piatti più venduti',
    resource: 'dishes-profit',
  },
];

export default function StatisticsPage() {
  const [from, setFrom] = useState(moment()
    .add(-1, 'months'));
  const [to, setTo] = useState(moment());

  const [option, setOption] = useState(Stats[0]);

  const [stats, setStats] = useState({ entries: [] });

  const [data, refresh] = useRemote(`statistics/${option.resource}/${from.format('YYYY-MM-DD')}/${to.format('YYYY-MM-DD')}`, {
    auto: false,
  });

  const [, printCategories] = useRemote(`statistics/print/categories/${from.format('YYYY-MM-DD')}/${to.format('YYYY-MM-DD')}`, {
    auto: false,
  });

  const [, printDishes] = useRemote(`statistics/print/dishes/${from.format('YYYY-MM-DD')}/${to.format('YYYY-MM-DD')}`, {
    auto: false,
  });

  const randomColor = useCallback(() => `#${Math.floor(Math.random() * 16777215)
    .toString(16)}`, []);

  useEffect(() => {
    if (data) {
      setStats({
        ...data,
        entries: data.entries.map(entry => ({
          ...entry,
          fill: randomColor(),
        })),
      });
    }
  }, [data, randomColor]);

  return (
    <Column grow>
      <Row spaced>
        <Column grow spaced>
          <DateInput label="Da" date={from} onDateChange={date => setFrom(date)} />
          <DateInput label="A" date={to} onDateChange={date => setTo(date)} />
        </Column>
        <Column grow spaced>
          <Button
            text="Ieri"
            onClick={() => {
              setFrom(moment().subtract(1, 'days'));
              setTo(moment().subtract(1, 'days'));
            }}
          />
          <Button
            text="Oggi"
            onClick={() => {
              setFrom(moment());
              setTo(moment());
            }}
          />
        </Column>
        <Column grow>
          <SelectEditor
            label="Statistica"
            value={option}
            id={opt => opt.resource}
            text={opt => opt.label}
            options={Stats}
            onSelectOption={opt => setOption(opt)}
          />
        </Column>
        <Column grow spaced>
          <Button
            text="Calcola"
            kind="primary"
            onClick={() => {
              refresh();
            }}
          />
          <Button
            text="Stampa"
            onClick={() => {
              if (option === Stats[0]) {
                printCategories();
              } else {
                printDishes();
              }
            }}
          />
        </Column>
      </Row>
      {stats
      && (
        <Row grow justifyCenter>
          <PieChart width={500} height={400}>
            <Pie
              isAnimationActive={false}
              nameKey="name"
              dataKey="value"
              data={stats.entries}
              label={({
                cx,
                cy,
                midAngle,
                innerRadius,
                outerRadius,
                value,
              }) => {
                const RADIAN = Math.PI / 180;
                // eslint-disable-next-line
                const radius = 25 + innerRadius + (outerRadius - innerRadius);
                // eslint-disable-next-line
                const x = cx + radius * Math.cos(-midAngle * RADIAN);
                // eslint-disable-next-line
                const y = cy + radius * Math.sin(-midAngle * RADIAN);

                return (
                  <text
                    x={x}
                    y={y}
                    fill="#000000"
                    textAnchor={x > cx ? 'start' : 'end'}
                    dominantBaseline="central"
                  >
                    {`${value}€`}
                  </text>
                );
              }}
            />
            <Legend />
          </PieChart>
          <PieChart width={500} height={400}>
            <Pie
              isAnimationActive={false}
              nameKey="name"
              dataKey="count"
              data={stats.entries}
              label
            />
            <Legend />
          </PieChart>
        </Row>
      )
      }
    </Column>
  );
}
