import React from 'react';
import Column from './Column';
import Button from './Button';

export default function ({...others}) {
  return (
    <Column {...others}>
      <Button invisible />
    </Column>
  );
}
