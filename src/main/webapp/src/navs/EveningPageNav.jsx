import React, { Fragment } from 'react';
import { beautifyDate } from '../utils/Utils';
import Button from '../widgets/Button';

export default function ({
  navigate, children, location, uri, eveningDate,
}) {
  const current = location.pathname === uri;
  return (
    <Fragment>
      <Button
        text={`Serata ${beautifyDate(eveningDate)}`}
        onClick={() => navigate('')}
        disabled={current}
        active={current}
      />
      {children}
    </Fragment>
  );
}
