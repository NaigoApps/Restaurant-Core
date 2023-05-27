import React from 'react';
import Column from './Column';
import Row from './Row';

export default function Wrap({ children, ...others }) {
  return (
    <Row {...others}>
      <Column>
        {children}
      </Column>
    </Row>
  );
}
