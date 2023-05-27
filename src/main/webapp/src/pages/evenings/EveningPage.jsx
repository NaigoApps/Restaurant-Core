import React from 'react';
import Column from '../../widgets/Column';
import Row from '../../widgets/Row';

export default function ({ children }) {
  return (
    <Row topSpaced grow>
      <Column grow>{children}</Column>
    </Row>
  );
}
