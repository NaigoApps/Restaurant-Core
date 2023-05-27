import React from 'react';
import Row from '../widgets/Row';
import Column from '../widgets/Column';

export default function () {
  return (
    <Row grow>
      <Column hCenter vCenter>
        <p className="h1">Pagina non trovata</p>
      </Column>
    </Row>
  );
}
