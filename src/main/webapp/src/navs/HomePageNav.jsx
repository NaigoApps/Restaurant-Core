import React, { Fragment } from 'react';
import Button from '../widgets/Button';

export default function ({
  children, navigate, location, uri,
}) {
  const current = location.pathname === uri;
  return (
    <Fragment>
      <Button
        text="Home"
        onClick={() => navigate('')}
        disabled={current}
        active={current}
      />
      {children}
    </Fragment>
  );
}
