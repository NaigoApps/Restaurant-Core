import React, { Fragment } from 'react';
import Button from '../widgets/Button';

export default function ({
  navigate, children, location, uri,
}) {
  const current = location.pathname === uri;
  return (
    <Fragment>
      <Button
        text="Configurazione"
        onClick={() => navigate('')}
        disabled={current}
        active={current}
      />
      {children}
    </Fragment>
  );
}
