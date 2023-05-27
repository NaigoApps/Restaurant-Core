import React, { Fragment } from 'react';
import Button from '../widgets/Button';

export default function DefaultNav({
  navigate, label, children, location, uri,
}) {
  const current = location.pathname === uri;
  return (
    <Fragment>
      <Button
        text={label}
        onClick={() => navigate('')}
        disabled={current}
        active={current}
      />
      {children}
    </Fragment>
  );
}
