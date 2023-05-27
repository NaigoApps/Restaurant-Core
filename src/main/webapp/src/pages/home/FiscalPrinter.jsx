import React, { Fragment } from 'react';
import useNetwork from '../../utils/useNetwork';
import useRemote from '../../utils/useRemote';
import Button from '../../widgets/Button';
import Column from '../../widgets/Column';
import Row from '../../widgets/Row';

export default function () {
  const { get } = useNetwork();

  async function feed() {
    get('printers/fiscal/feed');
  }

  // async function cut() {
  //   get('printers/fiscal/cut');
  // }

  async function open() {
    get('printers/fiscal/open');
  }

  const [data] = useRemote('printers/fiscal/status');


  if (!data) {
    return <p>Loading</p>;
  }

  return (
    <Fragment>
      <Row>
        <Column size="3">
          {data.map(entry => <p>{`${entry.key}: ${entry.value}`}</p>)}
        </Column>
        <Column size="1">
          <Row>
            <Column>
              <Button text="Feed" onClick={feed} />
            </Column>
          </Row>
          <Row>
            <Column>
              <Button text="Apri cassetto" onClick={open} />
            </Column>
          </Row>
        </Column>
      </Row>
    </Fragment>
  );
}
